package com.image.characterrecognition.module;




public interface MainContract {

    interface View{
        void getTokenSuccess();
        void updateUI(String s);
        void showDialog();

        void hideDialog();
        void errorMessage(String mesage);
    }

    interface Presenter{
        void getAccessToken();
        void getRecognitionResultByImage(String bitmap);
        void clear();
    }

}
