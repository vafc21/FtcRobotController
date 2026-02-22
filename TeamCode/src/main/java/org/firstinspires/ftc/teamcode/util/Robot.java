package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Handoff;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.subsystems.Vision;

public class Robot {
    public final MecanumDrive drive;
    public final Intake intake;
    public final Outtake outtake;
    public final Handoff handoff;
    public final Vision vision;
    public ElapsedTime runtime = new ElapsedTime();
    public final double outtakekp = 0.013;
    private final double rotatekp = 0.05;
    private final double strafekp = 0.02;
    // Top RPM should less for backspin
    public final double rpmMinTop = 950;
    public final double rpmMaxTop = 1900;
    // Bottom RPM should more for backspin
    public final double rpmMinBottom = 2170;
    public final double rpmMaxBottom = 2400;
    public final double longBottomRPM = 2200;
    public final double longTopRPM = 1100;
    public PIDController OuttakePID = new PIDController(outtakekp);
    public PIDController RotatePID = new PIDController(rotatekp);
    //private PIDController StrafePID = new PIDController(strafekp);
    public String color = null;
    public Robot(HardwareMap hw, Pose2d startPose){
        drive = new MecanumDrive(hw, startPose);
        handoff = new Handoff(hw);
        intake = new Intake(hw);
        outtake = new Outtake(hw);
        vision= new Vision(hw);
    }
    public void findColor(){
        LLResultTypes.FiducialResult team = vision.getLatestTeam();
        if (team!=null){
            int id = team.getFiducialId();
            if (id==20){
                color = "blue";
            } else if (id==24) {
                color = "red";
            }
        }

    }
}
