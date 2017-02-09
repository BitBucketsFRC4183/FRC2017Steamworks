#if ! defined (NEOPIXELDISPLAYCOMMANDS_H)
#define NEOPIXELDISPLAYCOMMANDS_H

#include "colors.h"

// Fill pixels all with a color
void fill (Adafruit_NeoPixel &strip, uint32_t color)
{
  for (uint16_t i = 0; i < strip.numPixels(); i++)
  {
    strip.setPixelColor(i, color);
  }
  strip.show();
}

// Fill the pixels one after the other with a color
void wipe (Adafruit_NeoPixel &strip, uint32_t color, uint16_t period)
{
  uint16_t n = strip.numPixels();
  uint16_t wait = period / n;
  for(uint16_t i = 0; i < n; i++) 
  {
    strip.setPixelColor(i, color);
    strip.show();
    delay(wait);
  }
}

// Flashing
void flash (Adafruit_NeoPixel &strip, uint32_t color, uint16_t cycles, uint16_t cyclePeriod)
{
  for (uint16_t cycle = 0; cycle < cycles; cycle++)
  {
      fill(strip, color);

      delay(cyclePeriod >> 1);

      fill(strip, BLACK);

      delay(cyclePeriod >> 1);
  }
}

// Chasing-Style decorative lights
void theaterChase (Adafruit_NeoPixel &strip, uint32_t color, uint16_t spacing, uint16_t cycles, uint16_t cyclePeriod)
{
  uint16_t n = strip.numPixels();
  uint16_t wait = cyclePeriod / spacing;
  for (uint16_t cycle = 0; cycle < cycles; cycle++)
  {
    for (uint16_t cycleProgress = 0; cycleProgress < spacing; cycleProgress++)
    {
      for (int i = 0; i < n; i+= spacing)
      {
        strip.setPixelColor(i + cycleProgress, color);
      }
      strip.show();

      delay(wait);

      for (int i = 0; i < n; i+= spacing)
      {
        strip.setPixelColor(i + cycleProgress, BLACK);
      }
    }
  }
}

#endif
