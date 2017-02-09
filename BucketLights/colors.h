/************************************************************************
Valid colors:

  BLACK, WHITE, GREY
  RED, GREEN, BLUE
  CYAN, MAGENTA, YELLOW
  ROSE, ORANGE; YELLOWGREEN, CYANGREEN; AZURE, VIOLET

Suffixes _LIGHT and _DARK work with all colors
************************************************************************/

#if ! defined (COLORS_H)
#define COLORS_H

#include <Adafruit_NeoPixel.h>  //  Verify that the library is included

//  RED, GREEN, BLUE(, WHITE)

//  Black, white, and greys

const uint32_t BLACK = Adafruit_NeoPixel::Color(0,0,0);
const uint32_t WHITE = Adafruit_NeoPixel::Color(255,255,255);
const uint32_t GREY = Adafruit_NeoPixel::Color(127,127,127);
const uint32_t GREY_DARK = Adafruit_NeoPixel::Color(64,64,64);
const uint32_t GREY_LIGHT = Adafruit_NeoPixel::Color(191,191,191);

//  Primaries

const uint32_t RED = Adafruit_NeoPixel::Color(255,0,0);
const uint32_t GREEN = Adafruit_NeoPixel::Color(0,255,0);
const uint32_t BLUE = Adafruit_NeoPixel::Color(0,0,255);

//  Secondaries

const uint32_t CYAN = Adafruit_NeoPixel::Color(0,255,255);
const uint32_t MAGENTA = Adafruit_NeoPixel::Color(255,0,255);
const uint32_t YELLOW = Adafruit_NeoPixel::Color(255,255,0);

//  Tertiaries

const uint32_t ROSE = Adafruit_NeoPixel::Color(255,0,127);        //  RED centered
const uint32_t ORANGE = Adafruit_NeoPixel::Color(255,127,0);

const uint32_t YELLOWGREEN = Adafruit_NeoPixel::Color(127,255,0); //  GREEN centered
const uint32_t CYANGREEN = Adafruit_NeoPixel::Color(0,255,127);

const uint32_t AZURE = Adafruit_NeoPixel::Color(0,127,255);       //  BLUE centered
const uint32_t VIOLET = Adafruit_NeoPixel::Color(127,0,255);

//  Variants

const uint32_t RED_DARK = Adafruit_NeoPixel::Color(127,0,0);
const uint32_t RED_LIGHT = Adafruit_NeoPixel::Color(255,127,127);

const uint32_t GREEN_DARK = Adafruit_NeoPixel::Color(0,127,0);
const uint32_t GREEN_LIGHT = Adafruit_NeoPixel::Color(127,255,127);

const uint32_t BLUE_DARK = Adafruit_NeoPixel::Color(0,0,127);
const uint32_t BLUE_LIGHT = Adafruit_NeoPixel::Color(127,127,255);


const uint32_t CYAN_DARK = Adafruit_NeoPixel::Color(0,127,127);
const uint32_t CYAN_LIGHT = Adafruit_NeoPixel::Color(127,255,255);

const uint32_t MAGENTA_DARK = Adafruit_NeoPixel::Color(127,0,127);
const uint32_t MAGENTA_LIGHT = Adafruit_NeoPixel::Color(255,127,127);

const uint32_t YELLOW_DARK = Adafruit_NeoPixel::Color(127,127,0);
const uint32_t YELLOW_LIGHT = Adafruit_NeoPixel::Color(255,255,127);


const uint32_t ROSE_DARK = Adafruit_NeoPixel::Color(127,0,64);
const uint32_t ROSE_LIGHT = Adafruit_NeoPixel::Color(255,127,191);

const uint32_t ORANGE_DARK = Adafruit_NeoPixel::Color(127,64,0);
const uint32_t ORANGE_LIGHT = Adafruit_NeoPixel::Color(255,191,127);

const uint32_t YELLOWGREEN_DARK = Adafruit_NeoPixel::Color(64,127,0);
const uint32_t YELLOWGREEN_LIGHT = Adafruit_NeoPixel::Color(191,255,127);

const uint32_t CYANGREEN_DARK = Adafruit_NeoPixel::Color(0,127,64);
const uint32_t CYANGREEN_LIGHT = Adafruit_NeoPixel::Color(127,255,191);

const uint32_t AZURE_DARK = Adafruit_NeoPixel::Color(0,64,127);
const uint32_t AZURE_LIGHT = Adafruit_NeoPixel::Color(127,191,255);

const uint32_t VIOLET_DARK = Adafruit_NeoPixel::Color(64,0,127);
const uint32_t VIOLET_LIGHT = Adafruit_NeoPixel::Color(191,127,255);

#endif
