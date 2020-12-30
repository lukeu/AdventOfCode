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

Timings as of completion (after some 'on the day' optimisations):

```
Name                                                 |    Total |  Parsing |   Part 1 |   Part 2
-----------------------------------------------------+----------+----------+----------+---------
Day 01 Report Repair                                 |     0.21 |     0.19 |        - |     0.01
Day 02 Password Philosophy                           |     0.52 |     0.52 |     0.00 |     0.00
Day 03 Toboggan Trajectory                           |     0.13 |     0.12 |        - |     0.01
Day 04 Passport Processing (input validation)        |     1.02 |     0.68 |     0.06 |     0.28
Day 05 Binary Boarding                               |     0.33 |     0.15 |     0.18 |     0.00
Day 06 Custom Customs                                |     0.36 |     0.23 |        - |     0.13
Day 07 Handy Haversacks (recursive bags)             |    26.01 |     1.42 |    24.59 |     0.01
Day 08 Handheld Halting (trivial asm)                |     0.60 |     0.27 |     0.00 |     0.32
Day 09 Encoding Error                                |     0.26 |     0.20 |     0.02 |     0.04
Day 10 Adapter Array                                 |     0.12 |     0.11 |     0.01 |     0.00
Day 11 Seating System (cellular automa 1)            |     6.60 |     0.13 |        - |     6.48
Day 12 Rain Risk (grid navigation)                   |     0.16 |     0.14 |        - |     0.02
Day 13 Shuttle Search                                |     0.11 |     0.10 |     0.00 |     0.00
Day 14 Docking Data (Floating bit-masks)             |    16.49 |     0.16 |    13.49 |     2.84
Day 15 Rambunctious Recitation (elf memory game)     |   388.77 |     0.01 |     0.01 |   388.75
Day 16 Ticket Translation                            |     2.22 |     1.07 |     0.33 |     0.83
Day 17 Conway Cubes (part 2 only)                    |   156.39 |     0.13 |        - |   156.26
Day 18 Operation Order (expression evaluation)       |     1.51 |     0.27 |     0.63 |     0.61
Day 19 Monster Messages (lexical token recognition)  |    37.60 |     0.65 |     5.38 |    31.57
Day 20 Jurassic Jigsaw (monochrome image processing) |    27.33 |     0.67 |    25.98 |     0.68
Day 21 Allergen Assessment (logic)                   |     1.21 |     0.29 |     0.90 |     0.02
Day 22 Crab Combat (recursive card game)             |    72.16 |     0.11 |     0.06 |    71.98
Day 23 Crab Cups (custom linked list)                |   969.40 |     0.17 |     0.05 |   969.18
Day 24 Lobby Layout (cellular automa 3)              |    12.12 |     0.65 |     0.01 |    11.46
Day 25 Combo Breaker                                 |     3.90 |     0.13 |     3.77 |        -
```

Measurements are:
 * On an i7-8700K with CPU frequency locked to 3.6 GHz
   * (setting CPU min=max=99% in Window's power options disables dynamic overclocking)
   * The timings are about 20% longer, but more stable
 * "warm" timings, as this is the metric of most interest to me.
   (If the code was used anywhere that performance matters it would be JIT compiled.)

Still TODO:
  * Some indication of variance. (I'm collecting a progressing of measurements over time,
    but currently just reporting the last.)

