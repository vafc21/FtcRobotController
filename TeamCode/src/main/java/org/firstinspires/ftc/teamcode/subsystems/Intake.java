package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    private final DcMotor IntakeMotor;
    private final double pow = .5;

    public Intake(HardwareMap hardwareMap) {
        IntakeMotor = hardwareMap.get(DcMotor.class,"IntakeMotor");
        IntakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    //public boolean getHasStopped(){ return hasStopped;}
    public void runMotor(double pow){
        IntakeMotor.setPower(pow);
    }
    public void stopMotor(){
        runMotor(0);
    }
    public void intake(){
        runMotor(pow);
    }
    public void outtake(){
        runMotor(-.3);
    }
    public class IntakeAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            intake();
            return false;
        }
    }
    public class OuttakeAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            outtake();
            return false;
        }
    }
    public class StopAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            stopMotor();
            return false;
        }
    }
    public Action intakeAction(){
        return new IntakeAction();
    }
    public Action outtakeAction(){
        return new OuttakeAction();
    }
    public Action stopAction(){
        return new StopAction();
    }
}
