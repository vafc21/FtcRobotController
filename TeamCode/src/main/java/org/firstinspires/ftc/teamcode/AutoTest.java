package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.Handoff;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Outtake;

@Autonomous(name = "org.firstinspires.ftc.teamcode.AutoTest")
public class AutoTest extends LinearOpMode {
    MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0,0,0));
    Intake intake = new Intake(hardwareMap);
    Outtake outtake = new Outtake(hardwareMap);
    Handoff handoff = new Handoff(hardwareMap);
    @Override
    public void runOpMode() throws InterruptedException {

        waitForStart();
        Actions.runBlocking(
                drive.actionBuilder(new Pose2d(0, 0, 0))
                        .stopAndAdd(new IntakeAction())
                        .lineToX(64)
                        .stopAndAdd(new IntakeAction())
                        .lineToX(0)
                        .stopAndAdd(new OutakeAction())
                        .lineToX(64)
                        .stopAndAdd(new OutakeAction())
                        .lineToX(0)
                        .stopAndAdd(new HandoffAction())
                        .lineToX(64)
                        .stopAndAdd(new HandoffAction())
                        .lineToX(0)
                        .stopAndAdd(new StoreAction())
                        .lineToX(64)
                        .stopAndAdd(new StoreAction())
                        .lineToX(0)
                        .stopAndAdd(new StopHandoff())
                        .lineToX(64)
                        .lineToX(0)
                        .build());
    }
    public class IntakeAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            intake.intake();
            return false;
        }
    }
    public class OutakeAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            return false;
        }
    }
    public class HandoffAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            handoff.handoff();
            return false;
        }
    }
    public class StoreAction implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            handoff.handoff();
            return false;
        }
    }
    public class StopHandoff implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            handoff.stopMotors();
            return false;
        }
    }
}
