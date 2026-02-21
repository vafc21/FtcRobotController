package org.firstinspires.ftc.teamcode;


import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Handoff;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.subsystems.Vision;
import org.firstinspires.ftc.teamcode.util.PIDController;
import org.firstinspires.ftc.teamcode.util.Robot;

@TeleOp(name="TeleOp_Main")
public class MainTeleOpMode extends LinearOpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private Pose2d StartPose = new Pose2d(0, 0, 0);
    private Robot robot;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        robot = new Robot(hardwareMap,StartPose);
        /*
        MecanumDrive Drive = new MecanumDrive(hardwareMap, StartPose);
        Intake intake = new Intake(hardwareMap);
        Outtake outtake = new Outtake(hardwareMap);
        Handoff handoff = new Handoff(hardwareMap);
        Vision vision = new Vision(hardwareMap);
         */
        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips


        // Wait for the game to start (driver presses START)
        waitForStart();
        runtime.reset();

        robot.intake.stopMotor();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry
            double DConstant=1;

            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
//            double rotateSpeed = 0.8;
            double rotateSpeed = 0.75;
            double turn = gamepad1.left_stick_x;
            double drive  =  gamepad1.left_stick_y;
            double rotate = gamepad1.right_stick_x * rotateSpeed;
            double bottomDisPow=0;
            double topDisPow=0;





            //intake.takeInToggle(handoff.toggleReturn(gamepad1.a));
            if (gamepad1.right_bumper || gamepad1.left_bumper || gamepad1.y){
                int id = robot.vision.getLatestId();
                if (gamepad1.right_bumper && (id==20 || id==24)){
                    double area = robot.vision.getLatestTa();
                    if(area>0.0041) {
                        bottomDisPow = mapAreaToRpmBottom(area);
                        topDisPow = mapAreaToRpmTop(area);
                        robot.outtake.runTopMotor(robot.OuttakePID.calculate(robot.outtake.getTopRPM(),topDisPow));
                        robot.outtake.runBottomMotor(robot.OuttakePID.calculate(robot.outtake.getBottomRPM(),bottomDisPow));
                    } else {
                        bottomDisPow = robot.longBottomRPM;
                        topDisPow = robot.longTopRPM;
                        robot.outtake.runTopMotor(robot.OuttakePID.calculate(robot.outtake.getTopRPM(),topDisPow));
                        robot.outtake.runBottomMotor(robot.OuttakePID.calculate(robot.outtake.getBottomRPM(),bottomDisPow));
                    }
                        double tx = robot.vision.getLatestTxDegreees();
                        if (tx != -1) {
                            // Auto-strafe and auto-rotate
                            drive = 0;
                            turn = 0;
                            //turn = StrafePID.calculate(0, tx);
                            rotate = robot.RotatePID.calculate(0, tx);
                        }
                        /*if (outtake.getBottomRPM()!=0 && outtake.getTopRPM()!=0 && outtake.getBottomRPM()==bottomDisPow && outtake.getTopRPM()==topDisPow){
                            handoff.handoff();
                            intake.outtake();
                        }*/
                } else if (gamepad1.left_bumper) {
                    //outtake.short_outtake();
                } else if (gamepad1.y){
                    robot.outtake.intake();
                }
            } else {
                robot.outtake.stopMotors();
            }
            if (gamepad1.a) {
                robot.handoff.store();
                robot.intake.intake();
            } else if (gamepad1.b && (bottomDisPow<=robot.outtake.getBottomRPM() && topDisPow<=robot.outtake.getTopRPM())) {
                robot.handoff.handoff();
                robot.intake.outtake();
            } else if (gamepad1.x) {
                robot.handoff.handoff();
                robot.intake.outtake();
            } else {
                //if (!gamepad1.right_bumper) {
                robot.handoff.stopMotors();
                robot.intake.stopMotor();
                //}
            }




//
//            // Send calculated power to wheels
//            FLMotor.setPower(FLpower * speedForDrive);
//            BLMotor.setPower(BLpower * speedForDrive);
//            BRMotor.setPower(BRpower * speedForDrive);
//            FRMotor.setPower(FRpower * speedForDrive);
            Vector2d translationalVelocity = new Vector2d(DConstant * -drive, DConstant * -turn);
            double rotationalVelocity = DConstant * -rotate;

            PoseVelocity2d velocity = new PoseVelocity2d(translationalVelocity, rotationalVelocity);

            robot.drive.setDrivePowers(velocity);


            // Show the elapsed game time and wheel power.
            //telemetry.addData("poseVX",Drive.updatePoseEstimate().linearVel.x);
            //telemetry.addData("posVY",Drive.updatePoseEstimate().linearVel.y);
            //telemetry.addData("AngV", Drive.updatePoseEstimate().angVel);
            //telemetry.addData("OuttakeBottomMotor POS",outtake.OuttakeBottomMotor.getCurrentPosition());
            //telemetry.addData("OuttakeBottomMotor POS Con",outtake.OuttakeBottomMotor.getCurrentPosition()/28);
            //telemetry.addData("kp: ",kp);
            telemetry.addData("OuttakeTopMotorRPM: ",robot.outtake.getTopRPM());
            telemetry.addData("OuttakeBottomMotorRPM: ",robot.outtake.getBottomRPM());
            telemetry.addData("Target X D", robot.vision.getLatestTxDegreees());
            telemetry.addData("Target Y D", robot.vision.getLatestTyDegreees());
            telemetry.addData("Target Area", robot.vision.getLatestTa());
            telemetry.addData("Target Id", robot.vision.getLatestId());
            //telemetry.addData("Target Team", vision.getLatestTeam().getFiducialId());
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            //telemetry.addData("Outtake Power 0.0-1.0: ", outtake_pow);
            //telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.update();
        }
    }

    private double mapAreaToRpmTop(double area) {
        if (area <= 0) {
            return 1000;
        }
        return robot.rpmMinTop + area * (robot.rpmMaxTop - robot.rpmMinTop);
    }
    private double mapAreaToRpmBottom(double area) {
        if (area <= 0) {
            return 1000;
        }
        return robot.rpmMinBottom + area * (robot.rpmMaxBottom - robot.rpmMinBottom);
    }


}
