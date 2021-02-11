# Recognition

The core of the recognition is based on the *ML Kit* from **Firebase**.
It is necessary to have the project registered on Firebase to get the *google-services.json* file needed to run the app.

The acquisition of the frames is done with **CameraX**. The *TextRecognitionActivity* class is in charge of launching CameraX with its preview and the analyzer for the recognition.

The analyzer is retrieving the frames so we can run them through our detectors. In our case, an OCR but also a CNN classifier. The analyzer is the *TextAnalyzer* class.

## Ocr

The OCR is that of google, present in the ML Kit. It is launched first on each frames to acquire lines of text.
When he gets a result, we have to query the database to find a match or even call the CNN classify if necessary. These operations are done in an asynchronous task called *SearchTask*.
Cards are broken down by lines, the more lines a card has that match the OCR result, the higher its recognition score. If there is a confusion between two cards because of an equivalent score. The task is cancelled because the OCR result is incomplete.

## Convolutional Neural Network Classifier

SearchTask can use a CNN classifier if it realizes that it is necessary. This is usually the case when several cards are recognized and one had its *cnn* field true.

Our CNN classifier is based on **MobileNet**, it uses a **TensorFlow Lite** model through Firebase.
MobileNet accepts a 224x244 RGB image as input. You can see the configuration in the *TfInterpreter* class. We only trained him to recognize four cards each with a figure on it (based on e3dcm1). So he identifies four classes :
- *e3dcm1q_1*
- *e3dcm1q_2*
- *e3dcm1q_3*
- *e3dcm1q_17*

The current model is only a prototype that we have trained with [Teachable Machine](https://teachablemachine.withgoogle.com/) on an augmented dataset. We didn't have time to produce a quality python script with Keras to do this task.