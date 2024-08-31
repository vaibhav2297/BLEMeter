package com.example.blemeter.config.constants

object Constants {


    //meter address hex : [41 73 0d 7a 8d e2]
    // number : 71 96 24 03 18 00 02

    //Hex string :  68 10 02 00 18 03 24 96 71 01 03 90  1F  D0  43  16
    val readMeterData = ubyteArrayOf(
        0x68u,  //start Frame
        0x10u,  //Meter Type
        0x02u,  //Address start
        0x00u,
        0x18u,
        0x03u,
        0x24u,  //Meter Type
        0x96u,
        0x71u,  //Address end
        0x01u,  // Control Code (C)
        0x03u,  //Data Length (L)
        0x90u,  //Data Identifier low
        0x1Fu,  //Data Identifier
        0xD0u,  //serial number
        0x43u,  // Checksum
        0x16u
    )

    const val hexReadMeterData = "6810020018032496710103901fd04316"

    const val valveOpen = "6810020018032496710407A017D855000000AF16"

    const val valveClose =  "6810020018032496710407A017D899000000F316"

    /* sale str & The number of purchases：1,Purchase quantity：10m³
    10 base array: [104, 16, 2, 0, 24, 3, 36, 150, 113, 4, 8, 160, 19, 0 , 1, 0,0,0,100,228,22]
    Hexadecimal： 68 10 02 00 18 03 24 96 71 04 08 A0 13 00 01 00 00 00 64 E4 16 */

}