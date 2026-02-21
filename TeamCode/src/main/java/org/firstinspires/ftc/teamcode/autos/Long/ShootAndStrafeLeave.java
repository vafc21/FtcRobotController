package org.firstinspires.ftc.teamcode.autos.Long;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.util.Robot;
import org.firstinspires.ftc.teamcode.util.RobotActions;

@Autonomous(name = "LONGShootAndStrafeLeave")
public class ShootAndStrafeLeave extends LinearOpMode  {
    private Robot robot;
    private RobotActions actions;
    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d startPose = new Pose2d(0, 0, 0);
        robot = new Robot(hardwareMap, startPose);
        actions = new RobotActions(robot);
        waitForStart();
        robot.findColor();
        Actions.runBlocking(
                robot.drive.actionBuilder(startPose)
                        .lineToX(5)
                        //.turn(20*((Math.PI)/180))
                        .build()
        );
        if (robot.color.equals("blue")){
            Actions.runBlocking(
                    robot.drive.actionBuilder(startPose)
                            //.lineToX(5)
                            .turn(22*((Math.PI)/180))
                            .build()
            );
        } else if (robot.color.equals("red")) {
            Actions.runBlocking(
                    robot.drive.actionBuilder(startPose)
                            //.lineToX(5)
                            .turn(-22*((Math.PI)/180))
                            .build()
            );
        }
        Actions.runBlocking( new ParallelAction(
                        actions.autoPos(),
                        actions.shoot(),
                        actions.handoff()
                )
        );
        if (robot.color.equals("blue")){
            Actions.runBlocking(
                    robot.drive.actionBuilder(startPose)
                            .turn(-20*((Math.PI)/180))
                            .build()
            );
        } else if (robot.color.equals("red")) {
            Actions.runBlocking(
                    robot.drive.actionBuilder(startPose)
                            .turn(20*((Math.PI)/180))
                            .build()
            );
        }
        Actions.runBlocking(
                robot.drive.actionBuilder(startPose)
                        .lineToX(-6.1)
                        .build()
        );
        robot.findColor();
        if (robot.color.equals("blue")){
            Actions.runBlocking(
                    robot.drive.actionBuilder(startPose)
                            .strafeTo(new Vector2d(0,13))
                            .build()
            );
        } else if (robot.color.equals("red")) {
            Actions.runBlocking(
                    robot.drive.actionBuilder(startPose)
                            .strafeTo(new Vector2d(0,-13))
                            .build()
            );
        }
    }

}
