 package com.tlg.admsayar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


 public class MainActivity extends AppCompatActivity implements SensorEventListener {


 TextView textViewcounter;
 TextView textViewSteptarget;
 SensorManager sensorManager;
 Sensor mStepCounter;
boolean CounterSensorexist;
ProgressBar progressBar;
EditText editText;
int previousSC=0;
Button button;
ImageButton ibbtn;
int stepCount=0;

int tarnumber=0;   //HEDEF İÇİN
int count=0 ;
int value=0;

int permission=1;       //İZİN İÇİN

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACTIVITY_RECOGNITION)== PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(MainActivity.this,"izin verildi",Toast.LENGTH_SHORT).show();
        }
        else
        {
            requestActivityPermision();
        }




        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);





        textViewcounter=findViewById(R.id.textView);
        textViewSteptarget=findViewById(R.id.textView3);
        button=findViewById(R.id.button);
        progressBar=findViewById(R.id.progressBar);
        ibbtn=findViewById(R.id.imageButton);
        editText=findViewById(R.id.editTextNumber2);



            ibbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    count=0;

                    String target=editText.getText().toString();
                    textViewSteptarget.setText(target);

                    if (editText.getText().toString().matches(""))
                    {
                        Toast.makeText(MainActivity.this,"hedef girin",Toast.LENGTH_LONG).show();    //HEDEF GİRİLMEMİŞSE GİRİLMESİ İÇİN
                    }
                    else  {
                         tarnumber= Integer.parseInt(textViewSteptarget.getText().toString());            //HEDEFİ PROGRES BARA VE HEDEFE ATMAK İÇİN
                        progressBar.setMax(tarnumber);
                    }

                }
            });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                previousSC=stepCount;
                progressBar.setProgress(0);
                textViewcounter.setText("0");             //PROGRES BARI VE ADIM SAYARI SIFIRLAMA
            }
        });


        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);  //cihazın sensör kısmına erişim sağlar

        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null)
        {
            mStepCounter=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

            CounterSensorexist=true;
        }
        else{
            Toast.makeText(getApplicationContext(),"Sensör Bulunamadı",Toast.LENGTH_LONG).show();     //SENSÖR BULUNAMAMASI
            CounterSensorexist=false;
        }

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor==mStepCounter)
        {
            stepCount= (int) event.values[0];
             value=stepCount-previousSC;

            textViewcounter.setText(String.valueOf(value));         //ADIM SAYACI GÜNCELLEME
            progressBar.setProgress(value);                       //PROGRES BARI GÜNCELLEME

            if (value>=tarnumber && count==0 ){

                Toast.makeText(getApplicationContext(),"Hedefinize ulaştınız",Toast.LENGTH_SHORT).show();      //BAŞARI MESAJI
                count=1;
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    protected void onResume() {
        super.onResume();

        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null)
        {
            sensorManager.registerListener(this,mStepCounter,SensorManager.SENSOR_DELAY_NORMAL);        //SENSÖRÜ ÇALIŞIRMA
        }



    }


    @Override
    protected void onPause() {
        super.onPause();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null)
        {
            sensorManager.unregisterListener(this,mStepCounter);               //ARKA PLANA ATILDIĞINDA SENSÖRÜ KAPATMA
        }
    }



     private void requestActivityPermision ()
     {
         if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACTIVITY_RECOGNITION))
         {
         new AlertDialog.Builder(this).setTitle("İzin Gerekiyor").setMessage("Adım sayarın çalışabilmesi için bu izine ihtiyaç vardır.")
                 .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACTIVITY_RECOGNITION},permission);  // İZİN İSTEME
             }
         })
                 .setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         dialog.dismiss();
                     }
                 })
                 .create().show();
         }

          else {
         ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACTIVITY_RECOGNITION},permission);
               }
    }

     @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         if (requestCode== permission )
         {
             if (grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED)
             {
                 Toast.makeText(this,"İzin verildi",Toast.LENGTH_SHORT).show();                     //İZİN İSTEME SONUC MESAJI
             }
             else
             {
                 Toast.makeText(this,"İzin verilmedi",Toast.LENGTH_SHORT).show();
             }
         }
     }
 }