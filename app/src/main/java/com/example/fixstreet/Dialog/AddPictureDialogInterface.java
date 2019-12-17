package com.example.fixstreet.Dialog;

import android.net.Uri;

public interface AddPictureDialogInterface {
    public void userSelectedApicture(Uri value);
    public void userTakenApicture(Uri value);


    public void userCanceled();
}
