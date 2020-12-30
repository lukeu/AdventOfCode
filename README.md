# AdventOfCode (2020)
2020 is my first AoC, and my first attempt at speed coding.

Getting up at 5:40am each day, I'm pretty groggy to start off with.
Still, I'm having a crack at coding fast, not clean, and then learning
what I should be doing to get faster at it.

I'm sticking with Java this year (as I have too much else going on ATM).
I have used https://projecteuler.net/ in the past
to dabble in other languages (mainly Python there)
In a future AoC I'd be keen to try some Rust, Kotlin, Scala 3...

## Executing

Requires Java 15, as the new `record` construct (preview feature)
was used in a few places.

To build and profile all puzzles:

`./gradlew run`

## Performance summary

Since completing the 2020 challenge, I've now retrofitted something of a
test/profile framework so that I can iterate on the performance of some
of the interesting problems and perhaps learn a few tricks.

Current Timings:

```
Name                                                 | Total ms |  Parsing |   Part 1 |   Part 2
-----------------------------------------------------+----------+----------+----------+---------
Day 01 Report Repair                                 |     0.21 |     0.20 |        - |     0.01
Day 02 Password Philosophy                           |     0.48 |     0.48 |     0.00 |     0.00
Day 03 Toboggan Trajectory                           |     0.13 |     0.12 |        - |     0.01
Day 04 Passport Processing (input validation)        |     1.09 |     0.76 |     0.06 |     0.27
Day 05 Binary Boarding                               |     0.34 |     0.15 |     0.19 |     0.00
Day 06 Custom Customs                                |     0.36 |     0.23 |        - |     0.13
Day 07 Handy Haversacks (recursive bags)             |    22.93 |     1.36 |    21.55 |     0.01
Day 08 Handheld Halting (trivial asm)                |     0.62 |     0.29 |     0.00 |     0.33
Day 09 Encoding Error                                |     0.24 |     0.18 |     0.02 |     0.04
Day 10 Adapter Array                                 |     0.12 |     0.11 |     0.01 |     0.00
Day 11 Seating System (cellular automa 1)            |     5.16 |     0.14 |        - |     5.02
Day 12 Rain Risk (grid navigation)                   |     0.16 |     0.13 |        - |     0.03
Day 13 Shuttle Search                                |     0.11 |     0.10 |     0.00 |     0.01
Day 14 Docking Data (Floating bit-masks)             |    17.10 |     0.16 |    14.06 |     2.88
Day 15 Rambunctious Recitation (elf memory game)     |   314.96 |     0.01 |     0.01 |   314.94
Day 16 Ticket Translation                            |     2.11 |     1.07 |     0.28 |     0.76
Day 17 Conway Cubes (part 2 only)                    |     7.14 |     0.13 |        - |     7.01
Day 18 Operation Order (expression evaluation)       |     1.49 |     0.20 |     0.66 |     0.63
Day 19 Monster Messages (lexical token recognition)  |    40.52 |     0.67 |     5.51 |    34.35
Day 20 Jurassic Jigsaw (monochrome image processing) |    27.52 |     0.68 |    26.14 |     0.69
Day 21 Allergen Assessment (logic)                   |     1.23 |     0.32 |     0.90 |     0.02
Day 22 Crab Combat (recursive card game)             |    73.92 |     0.14 |     0.06 |    73.72
Day 23 Crab Cups (custom linked list)                |   979.34 |     0.19 |     0.05 |   979.10
Day 24 Lobby Layout (cellular automa 3)              |    12.22 |     0.70 |     0.01 |    11.51
Day 25 Combo Breaker                                 |     3.92 |     0.14 |     3.78 |        -

TOTAL: 1513 ms
```

Today's optimisations:
 * Day 11: 6.6 -> 5.2 ms
 * Day 15: 388 -> 315 ms
 * Day 17: 156 -> 7 ms (still a few more ideas)

Measurements are:
 * On an i7-8700K with CPU frequency locked to 3.6 GHz
   * (setting CPU min=max=99% in Window's power options disables dynamic overclocking)
   * The timings are about 20% longer, but more stable
 * "warm" timings, as this is the metric of most interest to me.
   (If the code was used anywhere that performance matters it would be JIT compiled.)

Still TODO:
  * Some indication of variance. (I'm collecting a progressing of measurements over time,
    but currently just reporting the last.)

