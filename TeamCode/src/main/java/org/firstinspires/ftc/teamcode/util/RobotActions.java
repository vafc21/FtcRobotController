package org.firstinspires.ftc.teamcode.util;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;

/**
 * Action factory / composition layer.
 *
 * Use small methods that return Action objects instead of making a new Action class for every button.
 */
public class RobotActions {
    private final Robot robot;

    public RobotActions(Robot robot) {
        this.robot = robot;
    }
    public class Shoot implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            double area = robot.vision.getLatestTa();
            double bottomDisPow;
            double topDisPow;
            if(area>0.0041) {
                bottomDisPow = mapAreaToRpmBottom(area);
                topDisPow = mapAreaToRpmTop(area);
                robot.outtake.runTopMotor(robot.OuttakePID.calculate(topDisPow, robot.outtake.getTopRPM()));
                robot.outtake.runBottomMotor(robot.OuttakePID.calculate(bottomDisPow, robot.outtake.getBottomRPM()));
            } else {
                bottomDisPow = 2000;
                topDisPow = 1100;
                robot.outtake.runTopMotor(robot.OuttakePID.calculate(topDisPow, robot.outtake.getTopRPM()));
                robot.outtake.runBottomMotor(robot.OuttakePID.calculate(bottomDisPow, robot.outtake.getBottomRPM()));
            }
            double tx = robot.vision.getLatestTxDegreees();
            while (true){
                if (tx == -1){
                    break;
                } else if (tx<-2 || tx>2) {
                    double rotateV = robot.RotatePID.calculate(0, tx);
                    robot.drive.setDrivePowers(new PoseVelocity2d(new Vector2d(0,0),rotateV));
                }
            }
            if (topDisPow<robot.outtake.getTopRPM()){
                double doneTimeMs = robot.runtime.milliseconds()+3000;
                while (robot.runtime.milliseconds()<doneTimeMs){
                    robot.handoff.handoff();
                }
                robot.handoff.stopMotors();
                return false;
            }
            return true;

        }
    }
    public Action shoot() {
        return new Shoot();
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
