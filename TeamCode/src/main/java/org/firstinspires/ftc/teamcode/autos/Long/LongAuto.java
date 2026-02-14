package org.firstinspires.ftc.teamcode.autos.Long;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.util.Robot;
import org.firstinspires.ftc.teamcode.util.RobotActions;

@Autonomous(name = "LongAuto")
public class LongAuto extends LinearOpMode {
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
                            .turn(20*((Math.PI)/180))
                            .build()
            );
        } else if (robot.color.equals("red")) {
            Actions.runBlocking(
                    robot.drive.actionBuilder(startPose)
                            //.lineToX(5)
                            .turn(-20*((Math.PI)/180))
                            .build()
            );
        }
        Actions.runBlocking( new ParallelAction(
                actions.autoPos(),
                actions.shoot(),
                actions.handoff()
                )
        );
        Actions.runBlocking(
                robot.drive.actionBuilder(startPose)
                        .lineToX(10)
                        //.turn(-20*((Math.PI)/180))
                        .build()
        );
    }
}

