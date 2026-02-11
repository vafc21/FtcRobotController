package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.roadrunner.Pose2d;
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
    private final double outtakekp = 0.01;
    private final double rotatekp = 0.01;
    private final double strafekp = 0.02;
    // Top RPM should less for backspin
    public final double rpmMinTop = 1100;
    public final double rpmMaxTop = 2500;
    // Bottom RPM should more for backspin
    public final double rpmMinBottom = 1850;
    public final double rpmMaxBottom = 2500;
    public PIDController OuttakePID = new PIDController(outtakekp);
    public PIDController RotatePID = new PIDController(rotatekp);
    private PIDController StrafePID = new PIDController(strafekp);
    public Robot(HardwareMap hw, Pose2d startPose){
        drive = new MecanumDrive(hw, startPose);
        handoff = new Handoff(hw);
        intake = new Intake(hw);
        outtake = new Outtake(hw);
        vision= new Vision(hw);
    }
}
