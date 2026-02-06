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

@TeleOp(name="TeleOp_Main")
public class MainTeleOpMode extends LinearOpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private final double outtakekp = -0.15;
    private final double rotatekp = 0.05;
    private final double strafekp = 0.02;
    // Top RPM should less for backspin
    private final double rpmMinTop = 1300;
    private final double rpmMaxTop = 2200;
    // Bottom RPM should more for backspin
    private final double rpmMinBottom = 1650;
    private final double rpmMaxBottom = 2500;
    private PIDController OuttakePID = new PIDController(outtakekp);
    private PIDController RotatePID = new PIDController(rotatekp);
    private PIDController StrafePID = new PIDController(strafekp);
    //    private DcMotor FRMotor = null;
//    private DcMotor FLMotor = null;
//    private DcMotor BRMotor;
//    private DcMotor BLMotor;

    private double outtake_pow=.65;

    private Pose2d StartPose = new Pose2d(0, 0, 0);


    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
//        FRMotor  = hardwareMap.get(DcMotor.class, "FrontRightMotor");
//        FLMotor = hardwareMap.get(DcMotor.class, "FrontLeftMotor");
//        BLMotor = hardwareMap.get(DcMotor.class, "BackLeftMotor");
//        BRMotor = hardwareMap.get(DcMotor.class, "BackRightMotor");.
        //Intake = hardwareMap.get(CRServo.class, "Intake");
        MecanumDrive Drive = new MecanumDrive(hardwareMap, StartPose);
        Intake intake = new Intake(hardwareMap);
        Outtake outtake = new Outtake(hardwareMap);
        Handoff handoff = new Handoff(hardwareMap);
        Vision vision = new Vision(hardwareMap);
        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips


        // Wait for the game to start (driver presses START)
        waitForStart();
        runtime.reset();

        intake.stopMotor();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry
            double DConstant=1;

            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
//            double rotateSpeed = 0.8;
            double rotateSpeed = 0.65;
            double turn = gamepad1.left_stick_x;
            double drive  =  gamepad1.left_stick_y;
            double rotate = -gamepad1.right_stick_x * rotateSpeed;


            if (gamepad1.a) {
                handoff.store();
                intake.intake();
            } else if (gamepad1.x) {
                handoff.handoff();
                intake.outtake();
            } else {
                if (!gamepad1.right_bumper) {
                    handoff.stopMotors();
                    intake.stopMotor();
                }
            }


            //intake.takeInToggle(handoff.toggleReturn(gamepad1.a));
            if (gamepad1.right_bumper || gamepad1.left_bumper || gamepad1.y){
                if (gamepad1.right_bumper){
                    double area = vision.getLatestTa();
                    double bottomDisPow = mapAreaToRpmBottom(area);
                    double topDisPow = mapAreaToRpmTop(area);
                    outtake.runTopMotor(OuttakePID.calculate(topDisPow,outtake.getTopRPM()));
                    outtake.runBottomMotor(OuttakePID.calculate(bottomDisPow,outtake.getBottomRPM()));
                    double tx = vision.getLatestTxDegreees();
                    if (tx != -1) {
                        // Auto-strafe and auto-rotate
                        drive = 0;
                        turn = 0;
                        //turn = StrafePID.calculate(0, tx);
                        rotate = RotatePID.calculate(0, -tx);
                    }
                    /*if (outtake.getBottomRPM()!=0 && outtake.getTopRPM()!=0 && outtake.getBottomRPM()==bottomDisPow && outtake.getTopRPM()==topDisPow){
                        handoff.handoff();
                        intake.outtake();
                    }*/
                    //outtake.long_outtake();
                    //outtake.runMotor(outtake_pow); //for testing
                } else if (gamepad1.left_bumper) {
                    //outtake.short_outtake();
                } else if (gamepad1.y){
                    outtake.intake();
                }
            } else {
                outtake.stopMotors();
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

            Drive.setDrivePowers(velocity);


            // Show the elapsed game time and wheel power.
            //telemetry.addData("poseVX",Drive.updatePoseEstimate().linearVel.x);
            //telemetry.addData("posVY",Drive.updatePoseEstimate().linearVel.y);
            //telemetry.addData("AngV", Drive.updatePoseEstimate().angVel);
            //telemetry.addData("OuttakeBottomMotor POS",outtake.OuttakeBottomMotor.getCurrentPosition());
            //telemetry.addData("OuttakeBottomMotor POS Con",outtake.OuttakeBottomMotor.getCurrentPosition()/28);
            //telemetry.addData("kp: ",kp);
            telemetry.addData("OuttakeTopMotorRPM: ",outtake.getTopRPM());
            telemetry.addData("OuttakeBottomMotorRPM: ",outtake.getBottomRPM());
            telemetry.addData("Target X D", vision.getLatestTxDegreees());
            telemetry.addData("Target Y D", vision.getLatestTyDegreees());
            telemetry.addData("Target Area", vision.getLatestTa());
            telemetry.addData("Target Id", vision.getLatestId());
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
        return rpmMinTop + area * (rpmMaxTop - rpmMinTop);
    }
    private double mapAreaToRpmBottom(double area) {
        if (area <= 0) {
            return 1000;
        }
        return rpmMinBottom + area * (rpmMaxBottom - rpmMinBottom);
    }


}
