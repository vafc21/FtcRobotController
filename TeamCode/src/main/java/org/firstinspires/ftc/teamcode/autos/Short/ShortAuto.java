package org.firstinspires.ftc.teamcode.autos.Short;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.util.Robot;
import org.firstinspires.ftc.teamcode.util.RobotActions;

@Autonomous(name = "ShortAuto")
public class ShortAuto extends LinearOpMode {
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
                        .lineToX(-35)
                        .build()
        );
        Actions.runBlocking( new ParallelAction(
                actions.autoPos(),
                actions.shoot(),
                actions.handoff()
                )
        );
        Actions.runBlocking(
                robot.drive.actionBuilder(startPose)
                        .stopAndAdd(actions.shortLeave())
                        .build()
        );
    }
}
