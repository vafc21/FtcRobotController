package org.firstinspires.ftc.teamcode.autos.Short;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.util.Robot;
import org.firstinspires.ftc.teamcode.util.RobotActions;

@Autonomous(name = "StrafeShortLeave")
public class StrafeShortLeave extends LinearOpMode  {
    private Robot robot;
    private RobotActions actions;
    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d startPose = new Pose2d(0, 0, 0);
        robot = new Robot(hardwareMap, startPose);
        actions = new RobotActions(robot);
        waitForStart();
        Actions.runBlocking(
                robot.drive.actionBuilder(startPose)
                        .lineToX(-25.5)
                        .build()
        );
        robot.findColor();
        if (robot.color.equals("blue")){
            Actions.runBlocking(
                    robot.drive.actionBuilder(startPose)
                            .turn(-145*((Math.PI)/180))
                            .lineToX(21)
                            .build()
            );
        } else if (robot.color.equals("red")) {
            Actions.runBlocking(
                    robot.drive.actionBuilder(startPose)
                            .turn(145*((Math.PI)/180))
                            .lineToX(21)
                            .build()
            );
        }
    }

}
