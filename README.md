# AdventOfCode (2020)
2020 is my first AoC, and my first attempt at speed coding.

Getting up at 5:40am each day, I'm pretty groggy to start off with.
Still, I'm having a crack at coding fast, not clean, and then learning
what I should be doing to get faster at it.

I'm sticking with Java this year (I have too much else going on ATM).
The code here requires Java 15 with `--enable-preview` as I have used
the new `record` construct in a few places.

I have used https://projecteuler.net/ in the past
to dabble in other languages (mainly Python there)
Maybe in a future AoC I'd be keen to try some Rust, Kotlin, Scala 3...

## Executing

With JDK 15 on your path, to build and profile all days just execute:

`./gradlew run`

## Performance summary

Since completing the 2020 challenge, I've now retrofitted something of a
test/profile framework so that I can iterate on the performance of some
of the interesting problems and perhaps learn a few tricks.

Here's some preliminary timings from my laptop:

```
Name                                                 | Total ms |  Parsing |   Part 1 |   Part 2
-----------------------------------------------------+----------+----------+----------+---------
Day 01 Report Repair                                 |     0.36 |     0.35 |        - |     0.01
Day 02 Password Philosophy                           |     0.79 |     0.79 |     0.00 |     0.00
Day 03 Toboggan Trajectory                           |     0.19 |     0.16 |        - |     0.03
Day 04 Passport Processing (input validation)        |     1.08 |     0.80 |     0.06 |     0.21
Day 05 Binary Boarding                               |     0.51 |     0.18 |     0.32 |     0.01
Day 06 Custom Customs                                |     0.34 |     0.24 |        - |     0.10
Day 07 Handy Haversacks (recursive bags)             |    23.40 |     1.07 |    22.31 |     0.01
Day 08 Handheld Halting (trivial asm)                |     0.86 |     0.53 |     0.00 |     0.32
Day 09 Encoding Error                                |     0.40 |     0.31 |     0.02 |     0.07
Day 10 Adapter Array                                 |     0.30 |     0.27 |     0.02 |     0.01
Day 11 Seating System (cellular automa 1)            |     6.55 |     0.21 |        - |     6.34
Day 12 Rain Risk (grid navigation)                   |     0.41 |     0.29 |        - |     0.13
Day 13 Shuttle Search                                |     0.18 |     0.17 |     0.01 |     0.01
Day 14 Docking Data (Floating bit-masks)             |    14.85 |     0.21 |    12.38 |     2.25
Day 15 Rambunctious Recitation (elf memory game)     |   301.77 |     0.01 |     0.01 |   301.75
Day 16 Ticket Translation                            |     2.06 |     1.07 |     0.25 |     0.74
Day 17 Conway Cubes (part 2 only)                    |   142.19 |     0.16 |        - |   142.02
Day 18 Operation Order (expression evaluation)       |     1.42 |     0.26 |     0.57 |     0.58
Day 19 Monster Messages (lexical token recognition)  |    34.64 |     0.64 |     4.90 |    29.10
Day 20 Jurassic Jigsaw (monochrome image processing) |    25.34 |     0.56 |    24.22 |     0.55
Day 21 Allergen Assessment (logic)                   |     1.19 |     0.31 |     0.86 |     0.03
Day 22 Crab Combat (recursive card game)             |    65.49 |     0.16 |     0.05 |    65.28
Day 23 Crab Cups (custom linked list)                |   801.69 |     0.18 |     0.03 |   801.47
Day 24 Lobby Layout (cellular automa 3)              |     9.76 |     0.65 |     0.01 |     9.10
Day 25 Combo Breaker                                 |     3.24 |     0.24 |     3.00 |        -
```

Note that these are 'warm' timings, as this is the metric of most interest to me. (If the code was used
anywhere that performance matters it would be JIT compiled.)

Still TODO:
  * Update from my PC, with locked  CPU frequency to give more stable numbers
  * Make my 'code warmer' less biased towards longer-running code
  * Some indication of variance. (Maybe also plot over time / repetitions?)

