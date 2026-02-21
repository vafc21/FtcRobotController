package org.firstinspires.ftc.teamcode.util;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.limelightvision.LLResultTypes;

public class RobotActions {
    private final Robot robot;
    private boolean readyToShoot;
    private boolean stopAuto;
    private Double handoffStartMs = null;
    private boolean handoffToggle;

    public RobotActions(Robot robot) {
        this.robot = robot;
        //robot.OuttakePID.setkP(-robot.outtakekp);
        this.readyToShoot = false;
        this.stopAuto = false;
        this.handoffToggle = false;
    }
    public class Shoot implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (stopAuto){
                robot.outtake.stopMotors();
                return false;
            }
            double area = robot.vision.getLatestTa();
            double bottomDisPow=0;
            double topDisPow=0;
            if (area!=-1){
                if(area>0.0041) {
                    bottomDisPow = mapAreaToRpmBottom(area);
                    topDisPow = mapAreaToRpmTop(area);
                    robot.outtake.runTopMotor(robot.OuttakePID.calculate(robot.outtake.getTopRPM(), topDisPow));
                    robot.outtake.runBottomMotor(robot.OuttakePID.calculate(robot.outtake.getBottomRPM(), bottomDisPow));
                } else {
                    bottomDisPow = robot.longBottomRPM;
                    topDisPow = robot.longTopRPM;
                    robot.outtake.runTopMotor(robot.OuttakePID.calculate(robot.outtake.getTopRPM(), topDisPow));
                    robot.outtake.runBottomMotor(robot.OuttakePID.calculate(robot.outtake.getBottomRPM(), bottomDisPow));
                }
            }
            if ((topDisPow!=0 && bottomDisPow!=0) && (topDisPow<robot.outtake.getTopRPM() && bottomDisPow<robot.outtake.getBottomRPM())){
                readyToShoot = true;
                //return false;
            }

            return true;

        }
    }
    public class AutoPos implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            double tx = robot.vision.getLatestTxDegreees();
            double rotateV;
            //int id = robot.vision.getLatestTeam().getFiducialId();
            double precision = 5;
            if (((tx>-200 && tx<-precision) || (tx<200 && tx>precision))) {
                rotateV = robot.RotatePID.calculate(0, tx);
                robot.drive.setDrivePowers(new PoseVelocity2d(new Vector2d(0, 0), rotateV));
                return true;
            } else {
                rotateV = 0;
                robot.drive.setDrivePowers(new PoseVelocity2d(new Vector2d(0, 0), rotateV));
                return false;
            }
        }
    }
    public class Handoff implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            //double delay = 4000;
            if (!readyToShoot) {
                return true;
            }
            if (handoffStartMs == null) {
                handoffStartMs = robot.runtime.milliseconds();
                robot.handoff.handoff();
            } else if (robot.runtime.milliseconds() - handoffStartMs >= 4000) {
                robot.handoff.stopMotors();
                stopAuto = true;
                handoffStartMs = null;
                return false;
            } else if ((robot.runtime.milliseconds() - handoffStartMs)%1000==0) {
                if (handoffToggle){
                    robot.handoff.handoff();
                    handoffToggle=false;
                } else {
                    robot.handoff.stopMotors();
                    handoffToggle=true;
                }
            }
            //readyToShoot = false;
            return true;
        }
    }
    public class ShortLeave implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            LLResultTypes.FiducialResult team = robot.vision.getLatestTeam();
            if(team!=null){
                int id=team.getFiducialId();
                double timer = robot.runtime.milliseconds()+1000;
                if(id==20){
                    robot.drive.setDrivePowers(new PoseVelocity2d(new Vector2d(0, 1), 0));
                } else if (id==24) {
                    robot.drive.setDrivePowers(new PoseVelocity2d(new Vector2d(0, -1), 0));
                }
                if (robot.runtime.milliseconds()<=timer){
                    return true;
                }
                return false;
            }
            return false;
        }
    }
    public Action shoot() {
        return new Shoot();
    }
    public Action autoPos() {
        return new AutoPos();
    }
    public Action handoff(){
        return new Handoff();
    }
    public Action shortLeave(){
        return new ShortLeave();
    }
    private double mapAreaToRpmTop(double area) {
        if (area <= 0) {
            return 1000;
        }
        return robot.rpmMinTop + area * (robot.rpmMaxTop - robot.rpmMinTop);
    }
    private double mapAreaToRpmBottom(double area) {
        if (area <= 0) {
            return 1000;
        }
        return robot.rpmMinBottom + area * (robot.rpmMaxBottom - robot.rpmMinBottom);
    }


}
