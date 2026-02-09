package org.firstinspires.ftc.teamcode.subsystems;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import android.app.Notification;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Handoff {
    private final DcMotor top;
    private final CRServo bottom;
    private boolean toggleV = false;
    private final double pow = .50;
    public Handoff(HardwareMap hardwareMap){
        top = hardwareMap.get(DcMotor.class, "TopHandoff");
        bottom = hardwareMap.get(CRServo.class, "BottomHandoff");
        top.setDirection(REVERSE);

    }
    public void runMotor(double top_pow, double bottom_pow){
        top.setPower(top_pow);
        bottom.setPower(Math.abs(bottom_pow));
    }

    public void setServoPower(double p) {
        bottom.setPower(p);
    }
    public void runMotor(double p){
        top.setPower(p);
        bottom.setPower(Math.abs(p));
    }
    public void stopMotors(){
        runMotor(0);
    }
    public void store(){
        runMotor(pow,1);
    }


    public void handoff(){
        runMotor(-pow,1);
    }
    public class StoreAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            store();
            return false;
        }
    }
    public class HandoffAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            handoff();
            return false;
        }
    }
    public class StopAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            stopMotors();
            return false;
        }
    }
    public Action storeAction(){
        return new StoreAction();
    }
    public Action handoffAction(){
        return new HandoffAction();
    }
    public Action stopAction(){
        return new StopAction();
    }


}
