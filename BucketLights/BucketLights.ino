
// ****************************************************************************************************
//
// BucketLights
//
// Example for using BucketLights to coordinate multiple NeoPixel strips
//
// ----------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------
// Copyright (c) 2017 - RocketRedNeck / Michael Kessel
//
// RocketRedNeck and MIT Licenses
//
// RocketRedNeck hereby grants license for others to copy and modify this source code for
// whatever purpose other's deem worthy as long as RocketRedNeck is given credit where
// where credit is due and you leave RocketRedNeck out of it for all other nefarious purposes.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
// ****************************************************************************************************

#include "BucketLights.h"

// Notes for Adafruit_NeoPixel class
//
// Parameter 1 = number of pixels in strip
// Parameter 2 = Arduino pin number (most are valid)
// Parameter 3 = pixel type flags (OPT.), add together as needed:
//   NEO_KHZ800  800 KHz bitstream (most NeoPixel products w/WS2812 LEDs)
//   NEO_KHZ400  400 KHz (classic 'v1' (not v2) FLORA pixels, WS2811 drivers)
//   NEO_GRB     Pixels are wired for GRB bitstream (most NeoPixel products)
//   NEO_RGB     Pixels are wired for RGB bitstream (v1 FLORA pixels, not v2)
//   NEO_RGBW    Pixels are wired for RGBW bitstream (NeoPixel RGBW products)

enum LightingObjects  // Pin assignments
{
    DRIVE_SUBSYSTEM = 3,
    GEAR_SUBSYSTEM  = 4,
    BALL_SUBSYSTEM  = 5,
    CLIMB_SUBSYSTEM = 6  
};

Adafruit_NeoPixel myStrips[] =
{
  Adafruit_NeoPixel(16, DRIVE_SUBSYSTEM, NEO_GRB + NEO_KHZ800),
  Adafruit_NeoPixel(16, GEAR_SUBSYSTEM,  NEO_GRB + NEO_KHZ800),
  Adafruit_NeoPixel(16, BALL_SUBSYSTEM,  NEO_GRB + NEO_KHZ800),
  Adafruit_NeoPixel(16, CLIMB_SUBSYSTEM, NEO_GRB + NEO_KHZ800)
};

BucketLight myLights(myStrips, LENGTH(myStrips));

// Run-time strip allocation is not required...

void setup()
{
  Serial.begin(38400);
  Serial.println("BucketLights Starting...");

  myLights.begin();
}

String command;
void loop()
{
  if (Serial.available() > 0)
  {
      command = Serial.readStringUntil('\r');
      myLights.parse(command);
  }

  myLights.process();

}


