package utils;

/**
 * Shows three Tabs with options to Scan, Decode and Encode QR codes using
 * services provided by "QR Droid"
 * 
 * _________________________________________________________________________________
 * This is part of "QRDroidServices", by DroidLa. If you're creating an Android app
 * which uses one or more services provided by "QR Droid", you can use this code for
 * free, and modify it as you need, for personal and commercial use.
 * 
 * Any other use of this code is forbidden.
 * 
 * @author DroidLa
 * @version 1.0
 */
public class Services  {
    //Actions
    public static final String SCAN = "la.droid.qr.scan";
    public static final String ENCODE = "la.droid.qr.encode";
    public static final String DECODE = "la.droid.qr.decode";

    //Parameters
    //SCAN / DECODE
    public static final String COMPLETE = "la.droid.qr.complete"; //Default: false
    //ENCODE
    public static final String CODE =  "la.droid.qr.code"; //Required
    public static final String SIZE = "la.droid.qr.size"; //Default: Fit screen
    //ENCODE / DECODE
    public static final String IMAGE =  "la.droid.qr.image"; //Default for encode: false / Required for decode

    //Result

    public static final String RESULT = "la.droid.qr.result";
}