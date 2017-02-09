// ****************************************************************************************************
//
// MicroTimer.h
//
// A class for using micros() with increased range (beyond 70 minutes) at the expense of a small
// amount of latency for a signed micro-second presicion timer.
//
// Each instance can have a unique "mark" that defines a zero-point, which is useful for measuring
// durations from the mark. At least one instance is recommended to simply keep track since power-on.
//
// NOTE: range is 64-bit at 1 microsecond lsb, however the actual resolution is 4 microsecond on a
// 16 MHz UNO and 8 microseconds on an 8 MHz LILYPAD.
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

class MicroTimer
{
  public:
    // ----------------------------------------------------------------------------------------------------
    // Constructor - implements vitual reset in a manner that is efficient at construction, yet transparent
    // in syntax.
    // ----------------------------------------------------------------------------------------------------
    MicroTimer()
    {
      reset(true);
    }
    
    // ----------------------------------------------------------------------------------------------------
    // reset - transparent interface for both constrution and general reset conditions
    // ----------------------------------------------------------------------------------------------------
    void reset(bool isConstructor = false)
    {
      if (isConstructor)
      {
        upperTime_ = 0;
        lowerTime_ = 0;
        lastLowerTime_ = 0;
      }
      
      unmark();
    }

    // ----------------------------------------------------------------------------------------------------
    // mark - makes the current absolute time the local zero such that read() is in terms of the mark.
    // ----------------------------------------------------------------------------------------------------
    void mark()
    {
      unmark();
      markTime_ = read();
    }

    // ----------------------------------------------------------------------------------------------------
    // unmark - removes any previous mark such that the read() is in terms of absolute time.
    // ----------------------------------------------------------------------------------------------------
    void unmark()
    {
      markTime_ = 0;
    }

    // ----------------------------------------------------------------------------------------------------
    // read - reads the desired time base, compensating for rollover and any marked times.
    //
    // The function returns signed values to allow simplified arithmetic in measuring time deltas for
    // which a negative time is allowable.
    //
    // NOTE: This read takes about 36 usec on an Uno Rev 3, but has the advantage that it manages the
    // rollover, thus we have a timer precise to approximately 0.036 msec but will not roll over for
    // a really long time (i.e., > 500,000 years), which is way better than every 70 minutes, with
    // sub-millisecond precision. A good compromise.
    // ----------------------------------------------------------------------------------------------------
    long long read()
    {
      lastLowerTime_ = lowerTime_;
      lowerTime_ = micros();
      if (lowerTime_ < lastLowerTime_)
      {
        ++upperTime_;
      }
      
      time_ = lowerTime_;
      time_ += ((long long)upperTime_)<<32;
      return time_ - markTime_;
    }

    // ----------------------------------------------------------------------------------------------------
    // print - sends the time value to the Serial interface
    //
    // This function is provided because Serial.print does not natively support long long (int64_t) types.
    // Adding support to the underlying Print interface through the stdio printf semantics consume much
    // valuable code space for minimal utility. Instead we provide this simple conversion
    //
    // NOTE: This function is still somewhat time consuming because of the wide numerical semantics on
    // and 8-bit processor and the use of a divide operator, but you've got to do what you've got to do
    // if you want to see the value in a meaningful form.
    // ----------------------------------------------------------------------------------------------------
    void print(long long aTime_us) // Pass by reference does not seem to work here
    {
      long long upperTime = aTime_us / 1000000000LL;
      if (upperTime != 0)
      {
        Serial.print((long)upperTime);
      }

      long sign = 1;
      if (upperTime < 0)
      {
        sign = -1;
      }
      Serial.print((long)(sign * (aTime_us - (upperTime * 1000000000LL))));
    }
    void print(void)
    {
      print(read()); 
    }
    void println(long long aTime_us)
    {
      print(aTime_us);
      Serial.println("");
    }
    void println(void)
    {
      print();
      Serial.println("");
    }
    
  protected:
    unsigned long upperTime_;
    unsigned long lowerTime_;
    long long time_;
    unsigned long lastLowerTime_;
    long long markTime_;
    
  private:
};
