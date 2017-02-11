// ****************************************************************************************************
//
// BucketLights.h
//
// A class for using multiple NeoPixels strips in a coordinated manner.
//
// The number of strips, the pin address of each strip, and the size of each strip is passed into 
// an instance of the BucketLights class. A simple serial protocol allows external reference
// to each strip, the desired function, brightness, color(s), and execution period (when needed).
//
// An internal MicroTimer instance keeps track of time so all strip states remain
// coordinated within the BucketLights instance.
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

#if ! defined (BUCKET_LIGHTS_H)
#define BUCKET_LIGHTS_H

#include "RedsMacros.h"
#include "MicroTimer.h"
#include "NeopixelDisplayCommands.h"
#ifdef __AVR__
  #include <avr/power.h>
#endif

class BucketLight
{
private:
protected:
    Adafruit_NeoPixel *pStrips_;
    const uint16_t NUM_STRIPS;
    MicroTimer  timer_;

    struct StripState
    {
        char  function;
        uint32_t color;
        uint16_t nspace;
        uint8_t  brightness;
        int16_t index;             // State index is used to remember what to do next
        int16_t indexDirection;    // State to keep track of which way are are counting
        long long period_usec;
        long long nextScheduled_usec;

        StripStates()
        {
          function = '0';   // Off
          color = BLACK;
          nspace = 0;
          brightness = 2; // Minimum visible
          index = 0;
          indexDirection = 1;
          period_usec = 0;  // 0 = infinity
          nextScheduled_usec = 0;
        }
    };
    StripState *pStripStates_;
    
public:
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
    BucketLight(Adafruit_NeoPixel *pStrips, const uint16_t aNumStrips)
    : pStrips_(pStrips)
    , NUM_STRIPS(aNumStrips)
    {
        pStripStates_ = new StripState[NUM_STRIPS];
    }

    // -------------------------------------------------------------------------
    // begin - initializes all of the strips for this object; minimum brightness
    // defaults to 2, and all pixels OFF.
    // -------------------------------------------------------------------------
    void begin(void)
    {
        for (uint16_t i = 0; i < NUM_STRIPS; ++i)
        {
            pStrips_[i].begin();
            pStrips_[i].setBrightness(pStripStates_[i].brightness);
            fill(pStrips_[i], BLACK);
        }
    }

    // -------------------------------------------------------------------------
    // translateColor - returns the color code corresponding the character code
    // BLACK is returned by default
    // -------------------------------------------------------------------------
    uint32_t translateColor(char aColor)
    {
        uint32_t retColor = BLACK;
        switch(aColor)
        {
        case 'W':
          retColor = WHITE;
          break;
        case 'R':
          retColor = RED;
          break;
        case 'G':
          retColor = GREEN;
          break;
        case 'B':
          retColor = BLUE;
          break;
        case 'C':
          retColor = CYAN;
          break;
        case 'M':
          retColor = MAGENTA;
          break;
        case 'Y':
          retColor = YELLOW;
          break;
        case 'O':
          retColor = ORANGE;
          break;
        case 'V':
          retColor = VIOLET;
          break;
        default:
          break;
        }

        return retColor;
    }

    // -------------------------------------------------------------------------
    // translateFunction - returns function character if valid or 'x' if invalid
    //
    // See parse(...) for more information
    // -------------------------------------------------------------------------
    char translateFunction(char aFunction)
    {
        char retFunction = aFunction;
        switch(aFunction)
        {
        case '0':
        case '1':
        case 'S':
        case 'B':
        case 'F':
        case 'R':
        case 'C':
        case '*':
          break;
        default:
          retFunction = 'x';
          break;
        }

        return retFunction;
    }
    // -------------------------------------------------------------------------
    // parse - parses a string to determine what operation should be scheduled
    // on which strip
    // -------------------------------------------------------------------------
    void parse(const String &aCommand)
    {
      if (aCommand == "?")
      {
          Serial.println("String format is simple: 'NFCnbbbpppp'");
          Serial.println("    N - Strip Number 0..9");
          Serial.println("    F - Function");
          Serial.println("        0 (zero) = off");
          Serial.println("        1      = Solid ON");
          Serial.println("        S      = Snore on 3 second period");
          Serial.println("        B      = Blink with period pppp msec");
          Serial.println("        F      = Forward Chase n pixels (n >= 2, 1 on and n-1 off) with period pppp msec");
          Serial.println("        R      = Reverse Change n pixels (n >= 2, 1 on and n-1 off) with period pppp msec");
          Serial.println("        C      = Cylon (1-light side-to-side) with update period pppp msec");
          Serial.println("        *      = Sparkles (random colors) changing at period pppp msec");
          Serial.println("    C - Color Code");
          Serial.println("        0 = black (OFF)");
          Serial.println("        W = white");
          Serial.println("        R = red");
          Serial.println("        G = green");
          Serial.println("        B = blue");
          Serial.println("        C = cyan");
          Serial.println("        M = magenta");
          Serial.println("        Y = yellow");
          Serial.println("        O = orange");
          Serial.println("        V = violet");
          Serial.println("    n - Used only in F and R functions to space the pixels (ignored in all other modes)");
          Serial.println("    bbb - Brightness from 000 to 255");
          Serial.println("    pppp - Period in milliseconds between transitions");
      }
      else
      {
        Serial.println(aCommand);

        if (aCommand.length() == 11)
        {
          unsigned long stripNum = aCommand.substring(0,1).toInt();  // Defaults to 0 if non-numeric passed
          char function = translateFunction(aCommand.charAt(1));
          
          if ((stripNum < NUM_STRIPS) &&
              (function != 'x'))
          {
              pStripStates_[stripNum].function = function;        
              pStripStates_[stripNum].color = translateColor(aCommand.charAt(2));
              pStripStates_[stripNum].nspace = aCommand.substring(3,4).toInt();
              pStripStates_[stripNum].brightness = aCommand.substring(4,7).toInt();
    
              // Always start the index at 0
              pStripStates_[stripNum].index = 0;
              pStripStates_[stripNum].indexDirection = 1;
              
              if (pStripStates_[stripNum].function == 'S')
              {
                pStripStates_[stripNum].period_usec = 2000000 / 100;  // Snoring is always on a 2 second boundary, split into 100 steps
              }
              else
              {
                  pStripStates_[stripNum].period_usec = ((long long)aCommand.substring(7,11).toInt()) * 1000;
              }
    
              // Schedule on the next available boundary aligned
              // for the period specified
              long long now_usec = timer_.read();
              
              pStripStates_[stripNum].nextScheduled_usec = now_usec - (now_usec % pStripStates_[stripNum].period_usec) + pStripStates_[stripNum].period_usec;
          }
        }
      }
    }

    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    void nextSnore(uint16_t i)
    {
      if (pStripStates_[i].index == 0)
      {
          pStripStates_[i].indexDirection = 1;
      }
      else if (pStripStates_[i].index == 100)
      {
          pStripStates_[i].indexDirection = -1;        
      }

      pStripStates_[i].index += pStripStates_[i].indexDirection;

      pStrips_[i].setBrightness( ((float)pStripStates_[i].index / 100.0) * pStripStates_[i].brightness);
      fill(pStrips_[i], pStripStates_[i].color);
    }
    
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    void nextBlink(uint16_t i)
    {
        pStripStates_[i].index++;

        if (pStripStates_[i].index & 0x1)
        {
          fill(pStrips_[i], pStripStates_[i].color);
        }
        else
        {
          fill(pStrips_[i], BLACK);
        }
    }
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    void nextForward(uint16_t i)
    {
      uint16_t n = pStrips_[i].numPixels();
      uint16_t nspace = (pStripStates_[i].nspace == 0)? n : pStripStates_[i].nspace;
      uint16_t modIndex = (pStripStates_[i].index++) % nspace;

      for (uint16_t pixel = 0; pixel < n; ++pixel)
      {
        if ((pixel % pStripStates_[i].nspace) == modIndex)
        {
            pStrips_[i].setPixelColor(pixel, pStripStates_[i].color);
        }
        else
        {
            pStrips_[i].setPixelColor(pixel, BLACK);
        }

        pStrips_[i].show();
      }        
    }
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    void nextReverse(uint16_t i)
    {
      uint16_t n = pStrips_[i].numPixels();
      uint16_t nspace = (pStripStates_[i].nspace == 0)? n : pStripStates_[i].nspace;
      uint16_t modIndex = (pStripStates_[i].index--) % pStripStates_[i].nspace;

      for (uint16_t pixel = 0; pixel < n; ++pixel)
      {
        if ((pixel % pStripStates_[i].nspace) == modIndex)
        {
            pStrips_[i].setPixelColor(pixel, pStripStates_[i].color);
        }
        else
        {
            pStrips_[i].setPixelColor(pixel, BLACK);
        }

        pStrips_[i].show();
      }        
    }
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    void nextCylon(uint16_t i)
    {
      pStripStates_[i].nspace = 0;

      if (pStripStates_[i].indexDirection > 0)
      {       
          nextForward(i);
   
          if (pStripStates_[i].index >= (pStrips_[i].numPixels()-1))
          {
            pStripStates_[i].indexDirection *= -1;
          }
      }
      else
      {
          nextReverse(i);

          if (pStripStates_[i].index <= 0)
          {
            pStripStates_[i].indexDirection *= -1;
          }        
      }
      
    }
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    void nextSparkle(uint16_t i)
    {
      uint16_t n = pStrips_[i].numPixels();
      for (uint16_t pixel = 0; pixel < n; ++pixel)
      {
        pStrips_[i].setBrightness(10 + (rand() % 15));
        pStrips_[i].setPixelColor(pixel, Adafruit_NeoPixel::Color(rand()%255,rand()%255,rand()%255));
      }
      pStrips_[i].show();
        
    }
    
    // -------------------------------------------------------------------------
    // process - sequence each strip to the next state
    // -------------------------------------------------------------------------
    void process(void)
    {
        for (uint16_t i = 0; i < NUM_STRIPS; ++i)
        {
            // Change the brightness immediately
            pStrips_[i].setBrightness(pStripStates_[i].brightness);
            
            // Only strips with non-zero period are considered 
            // for periodic processing
            if (pStripStates_[i].period_usec != 0)
            {
                // Get the time "now" so we can maintain a schedule
                // for this strip (does not synchronize strips)
                // Individual strip functions must take less than
                // their commanded period; since we only allow
                // whole milliseconds on the interface we SHOULD
                // always be able to complete a state update within
                // a period and never lose schedule.
                long long now_usec = timer_.read();
                
                if (now_usec >= pStripStates_[i].nextScheduled_usec)
                {

                    pStripStates_[i].nextScheduled_usec += pStripStates_[i].period_usec;
                    
                    // Time is up for this strip
                    // Switch for each function (even the non-periodic ones)
                    switch(pStripStates_[i].function)
                    {
                    case '0':
                        fill(pStrips_[i],BLACK);
                        break;
                    case '1':
                        fill(pStrips_[i],pStripStates_[i].color);
                        break;
                    case 'S':   // Snore on 2 second period
                        nextSnore(i);
                        break;
                    case 'B':   // Blink with period pppp msec
                        nextBlink(i);
                        break; 
                    case 'F':   // Forward Chase n pixels (n >= 2, 1 on and n-1 off) with period pppp msec
                        nextForward(i);                 
                        break;
                    case 'R':   // Reverse Change n pixels (n >= 2, 1 on and n-1 off) with period pppp msec
                        nextReverse(i);
                        break;
                    case 'C':   // Cylon (1-light side-to-side) with update period pppp msec
                        nextCylon(i);
                        break;
                    case '*':   // Sparkles (random colors) changing at period pppp msec
                        nextSparkle(i);
                        break;
                        
                    default:
                        // Do nothing for all others
                        break;
                    }
                }
            }
            else  // Non-periodic processing
            {
              // Switch for non-periodic function
              switch(pStripStates_[i].function)
              {
              case '0':
                  fill(pStrips_[i],BLACK);
                  break;
              case '1':
                  fill(pStrips_[i],pStripStates_[i].color);
                  break;
              default:
                  // Do nothing for all other functions and errors
                  break;
              }
            }
        }
    }

};



#endif


