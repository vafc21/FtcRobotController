package org.firstinspires.ftc.teamcode.util;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
public class RobotActions {
    private final Robot robot;

    public RobotActions(Robot robot) {
        this.robot = robot;
    }
    public class Shoot implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
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
                    bottomDisPow = 1500;
                    topDisPow = 1100;
                    robot.outtake.runTopMotor(robot.OuttakePID.calculate(robot.outtake.getTopRPM(), topDisPow));
                    robot.outtake.runBottomMotor(robot.OuttakePID.calculate(robot.outtake.getBottomRPM(), bottomDisPow));
                }
            }
            if (topDisPow!=0 && bottomDisPow!=0 && topDisPow<robot.outtake.getTopRPM() && bottomDisPow<robot.outtake.getBottomRPM()){
                double doneTimeMs = robot.runtime.milliseconds()+5000;
                double allDoneTime = doneTimeMs+4000;
                //robot.outtake.runBottomMotor(0.5);
                //robot.outtake.runTopMotor(0.);
                while (robot.runtime.milliseconds()<doneTimeMs){
                    robot.handoff.handoff();
                }
                robot.handoff.stopMotors();
                while (allDoneTime>robot.runtime.milliseconds()){
                    ;
                }
                robot.outtake.stopMotors();
                return false;
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
            if (((tx>-200 && tx<-10) || (tx<200 && tx>10))) {
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
    public Action shoot() {
        return new Shoot();
    }
    public Action autoPos() {
        return new AutoPos();
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
