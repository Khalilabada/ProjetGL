package com.boky.PFE.factory.evaluation;


import com.boky.PFE.Beans.SaveEvaluation;
import com.boky.PFE.Beans.SaveEvaluationFDM;
import lombok.Builder;

public interface IEvaluation {
    Long getId();
    String getDate();
    String getType();
    default void remplirDepuisRequest(SaveEvaluation model) {}

    default void remplirDepuisFDM(SaveEvaluationFDM model) {}
}
