package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Outtake {
    private final DcMotorEx OuttakeTopMotor;
    private final DcMotorEx OuttakeBottomMotor;
    private final double short_pow = .50;
    private final double long_pow = 1.0;
    private final double top_modifier = 0.1;
    private final double TICKS_PER_REV = 28;

    public Outtake(HardwareMap hardwareMap){
        OuttakeTopMotor = hardwareMap.get(DcMotorEx.class,"OuttakeTopMotor");
        OuttakeBottomMotor = hardwareMap.get(DcMotorEx.class,"OuttakeBottomMotor");
    }
    public void runMotor(double top_pow,double bottom_pow){
        OuttakeTopMotor.setPower(top_pow-(top_pow*top_modifier));
        OuttakeBottomMotor.setPower(bottom_pow);
    }
    public void runMotor(double p){
        OuttakeTopMotor.setPower(p-(p*top_modifier));
        OuttakeBottomMotor.setPower(p);
    }
    public void runTopMotor(double p){
        OuttakeTopMotor.setPower(p);
    }
    public void runBottomMotor(double p){
        OuttakeBottomMotor.setPower(p);
    }
    public void stopMotors(){
        runMotor(0,0);
    }
    public void intake(){
        runMotor(-1);
    }
    public double getTopRPM(){
        return Math.abs((OuttakeTopMotor.getVelocity()/TICKS_PER_REV) * 60);
    }
    public double getBottomRPM() {
        return Math.abs((OuttakeBottomMotor.getVelocity() / TICKS_PER_REV) * 60);
    }
    public class IntakeAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            intake();
            return false;
        }
    }
    public class OuttakeTopAction implements Action {
        double power;
        private OuttakeTopAction(double power){
            this.power = power;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            runTopMotor(power);
            return false;
        }
    }
    public class OuttakeBottomAction implements Action {
        double power;
        private OuttakeBottomAction(double power){
            this.power = power;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            runBottomMotor(power);
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
    public Action intakeAction(){
        return new IntakeAction();
    }
    public Action outtakeTopAction(double power){
        return new OuttakeTopAction(power);
    }
    public Action outtakeBottomAction(double power){
        return new OuttakeBottomAction(power);
    }
    public Action stopAction(){
        return new StopAction();
    }

}
