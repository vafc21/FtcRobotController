package org.firstinspires.ftc.teamcode.subsystems;

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
        runMotor(-0.5);
    }
}
