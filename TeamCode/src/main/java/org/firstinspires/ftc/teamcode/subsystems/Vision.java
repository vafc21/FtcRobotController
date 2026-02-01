package org.firstinspires.ftc.teamcode.subsystems;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;

public class Vision {
    Limelight3A limelight;
    public Vision(HardwareMap hardwareMap){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.start();
        limelight.pipelineSwitch(0);
    }
    public LLResult getResult(){
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()){
            return result;
        }
        return null;
    }
    public int getLatestId(){
        if (getLatests()!=null){
            return getLatest().getFiducialId();
        }
        return -1;
    }
    public LLResultTypes.FiducialResult getLatest(){
        if (getLatests()!=null){
            return getLatests().get(0);
        }
        return null;
    }
    public List<LLResultTypes.FiducialResult> getLatests(){
        if (getResult() != null && !getResult().getFiducialResults().isEmpty()) {
            return getResult().getFiducialResults();
        }
        return null;
    }
    public double getLatestTxDegreees(){
        if (getLatest()!=null){
            return getLatest().getTargetXDegrees();
        }
        return -1;
    }
    public double getLatestTyDegreees(){
        if (getLatest()!=null){
            return getLatest().getTargetYDegrees();
        }
        return -1;
    }
    public double getLatestTa(){
        if (getLatest()!=null){
            return getLatest().getTargetArea();
        }
        return -1;
    }
}
