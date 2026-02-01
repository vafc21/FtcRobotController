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
    private final double kp = -0.15;
    private PIDController PID = new PIDController(kp);
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

            double delayMsec = 200;
            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            // leftPower  = -gamepad1.left_stick_y ;
            // rightPower = -gamepad1.right_stick_y ;

            //intake.intake(gamepad1.a, gamepad1.left_bumper);
            /*if (gamepad1.dpad_down){
                intake.stopMotor();
                handoff.stopMotors();
            }*/
//            handoff.handoff(gamepad1.x);
//            handoff.store(gamepad1.a);
            if (gamepad1.a) {
                handoff.store();
                intake.intake();
            } else if (gamepad1.x) {
                handoff.handoff();
                intake.outtake();
            } else {
                handoff.stopMotors();
                intake.stopMotor();
            }

            //intake.intake(gamepad1.a,gamepad1.x);
            // for testing!
            /*double vd = runtime.milliseconds();
            if (gamepad1.dpad_up && vd==runtime.milliseconds()) {
                outtake_pow += .05;
                vd+=delayMsec;
            } else if (gamepad1.dpad_down && vd==runtime.milliseconds()) {
                outtake_pow-=.05;
                vd+=delayMsec;
            }*/

            //intake.takeInToggle(handoff.toggleReturn(gamepad1.a));
            if (gamepad1.right_bumper || gamepad1.left_bumper || gamepad1.y){
                if (gamepad1.right_bumper){
                    double bottomDisPow = 1000;
                    double topDisPow = 1000;
                    outtake.runTopMotor(PID.calculate(topDisPow,outtake.getTopRPM()));
                    outtake.runBottomMotor(PID.calculate(bottomDisPow,outtake.getBottomRPM()));
                    //outtake.long_outtake();
                    //outtake.runMotor(outtake_pow); //for testing
                } else if (gamepad1.left_bumper) {
                    outtake.short_outtake();
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
            //telemetry.addData("OuttakeTopMotorRPM: ",outtake.getTopRPM());
            //telemetry.addData("OuttakeBottomMotorRPM: ",outtake.getBottomRPM());
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
}