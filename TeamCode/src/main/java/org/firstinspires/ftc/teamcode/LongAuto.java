package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.util.Robot;
import org.firstinspires.ftc.teamcode.util.RobotActions;

@Autonomous(name = "org.firstinspires.ftc.teamcode.LongAuto")
public class LongAuto extends LinearOpMode {
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
                        .lineToX(5)
                        .turn(20*((Math.PI)/180))
                        //.stopAndAdd(actions.autoPos())
                        //.stopAndAdd(actions.shoot())
                        .build()
        );
        Actions.runBlocking( new ParallelAction(
                actions.autoPos(),
                actions.shoot(),
                actions.handoff()
                )
        );
    }
}

