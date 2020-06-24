package com.universitedebordeaux.jumathsji.ocr;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.custom.FirebaseCustomLocalModel;
import com.google.firebase.ml.custom.FirebaseModelDataType;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInputs;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelInterpreterOptions;
import com.google.firebase.ml.custom.FirebaseModelOutputs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

// Infer the CNN classifier.
public class TfInterpreter {
    private FirebaseModelInterpreter mInterpreter;
    private FirebaseModelInputOutputOptions mInputOutputOptions;

    // MobileNet Input : Image 224x244 RGB
    private float[][][][] mInput = new float[1][224][224][3];

    private List<String> mLabelList;

    public TfInterpreter(String model_path, List<String> labelList) {
        mLabelList = labelList;

        // Firebase can run TensorFlow Lite model.
        FirebaseCustomLocalModel localModel = new FirebaseCustomLocalModel.Builder().setAssetFilePath(model_path).build();

        try {
            FirebaseModelInterpreterOptions options = new FirebaseModelInterpreterOptions.Builder(localModel).build();
            mInterpreter = FirebaseModelInterpreter.getInstance(options);
        } catch (FirebaseMLException e) {
            Log.e("TfInterpreter", "Error loading model");
        }

        try {
            mInputOutputOptions = new FirebaseModelInputOutputOptions.Builder()
                    .setInputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 224, 224, 3})
                    // Currently, we use four classes.
                    .setOutputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 4})
                    .build();
        } catch (FirebaseMLException e) {
            Log.e("TfInterpreter", "Error setup tensor io");
        }
    }

    // Transforms a bitmap picture into MobileNet Input.
    public void prepareTensorImage(Bitmap image) {
        Bitmap bitmap = Bitmap.createScaledBitmap(image, 224, 224, true);
        int batchNum = 0;
        for (int x = 0; x < 224; x++) {
            for (int y = 0; y < 224; y++) {
                int pixel = bitmap.getPixel(x, y);
                // Normalize channel values to [-1.0, 1.0]. Required by MobileNet.
                mInput[batchNum][x][y][0] = (Color.red(pixel) - 127) / 128.0f;
                mInput[batchNum][x][y][1] = (Color.green(pixel) - 127) / 128.0f;
                mInput[batchNum][x][y][2] = (Color.blue(pixel) - 127) / 128.0f;
            }
        }
    }

    // Runs the classifier and return one identifier (label).
    // Don't forget to call prepareTensorImage first.
    public String predict() {
        FirebaseModelOutputs result = null;
        try {
            FirebaseModelInputs inputs = new FirebaseModelInputs.Builder().add(mInput).build();
            Task<FirebaseModelOutputs> task = mInterpreter.run(inputs, mInputOutputOptions);

            // run synchronous.
            Tasks.await(task);

            result = task.getResult();
        } catch (FirebaseMLException e) {
            Log.e("TfInterpreter", "Error inference");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return result == null ? "null" : getLabel(result);
    }

    // Get the label from the class with the highest probability.
    private String getLabel(FirebaseModelOutputs result) {
        float[][] output = result.getOutput(0);
        float[] probabilities = output[0];

        Map<String, Float> labelResultMap = new HashMap<>();

        // First, apply label in order.
        for (int i = 0; i < probabilities.length; ++i)
            labelResultMap.put(mLabelList.get(i), probabilities[i]);

        // Second, get the max.
        return labelResultMap.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).get();
    }
}
