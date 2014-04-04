jamsa-mobile
============

Blind helper project (Android version)


Overview
--------

This project implements the client side of the Jamsa Project.
This software runs in an Android platform version 3.1 or higher.

The software acts as a doble client:

	* On one side, it reads data from an external sensor
	* On the other side, it requests information from a server

See 'references' for other software components.


Components
----------

* ID-20LA RFID sensor. RFID sensor with integrated antenna.
 * https://www.sparkfun.com/products/11828

* SEN-09963. USB towith FTDI chip
 * https://www.sparkfun.com/products/9963

* Mobile device
 * Samung Galaxy S3/Samsung Galaxy Nexus/Nexus 5

* Cable

* Gloves :)


Design
------

All the read operations (from the sensor and from the server) are
executed asynchronously. This way, we prevent the blocking of the UI.

The communication with the server is done via JSON documents to provide
a simple, yet powerful communication mechanism.

We use the TTS (Text to Speech) and the USBHost API from the stock Android
ADK.

In addition we use the USB-serial-for-android library under the terms
of the LGPL 2.1

As a little hack, we had to create the file device_filter.xml and add the 
following content in order to get our Serial to USB board recognized:

```
<!--  0x0403 / 0xabcd: Custom  -->
<usb-device vendor-id="1027" product-id="43981"/>
```


Download our [Android app](https://github.com/jamsa-project/jamsa-mobile/blob/master/bin/JamsaProject.apk) now!

References
----------

* Library USB-serial-for-android (LGPL 2.1)
  * https://code.google.com/p/usb-serial-for-android/
