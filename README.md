# AdventOfCode (2020)
2020 is my first AoC, and my first attempt at speed coding.

Getting up at 5:40am each day, I'm pretty groggy to start off with.
Still, I'm having a crack at coding fast, not clean, and then learning
what I should be doing to get faster at it.

I'm sticking with Java this year (as I have too much else going on ATM).
I have used https://projecteuler.net/ in the past
to dabble in other languages (mainly Python there)
In a future AoC I'd be keen to try some Julia, Rust, Kotlin, Scala 3...

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
Name                                                 |  Total μs |  Parsing |   Part 1 |   Part 2
-----------------------------------------------------+-----------+----------+----------+---------
Day 01 Report Repair                                 |        27 |       25 |        1 |        1  *
Day 02 Password Philosophy                           |        78 |       77 |        1 |        0  *
Day 03 Toboggan Trajectory                           |        15 |       11 |        1 |        3  *
Day 04 Passport Processing (input validation)        |       624 |      342 |       61 |      221
Day 05 Binary Boarding                               |       273 |       65 |      206 |        1
Day 06 Custom Customs                                |       329 |      167 |        - |      162
Day 07 Handy Haversacks (recursive bags)             |    18,960 |    1,391 |   17,558 |       12
Day 08 Handheld Halting (trivial asm)                |       404 |      125 |        4 |      275
Day 09 Encoding Error                                |       147 |       75 |       19 |       53
Day 10 Adapter Array                                 |        33 |       23 |        7 |        4
Day 11 Seating System (cellular automa 1)            |     2,383 |       36 |        - |    2,347 *
Day 12 Rain Risk (grid navigation)                   |        51 |       31 |        - |       21
Day 13 Shuttle Search                                |        16 |       11 |        1 |        4
Day 14 Docking Data (Floating bit-masks)             |    13,626 |       63 |   11,499 |    2,064
Day 15 Rambunctious Recitation (elf memory game)     |   241,867 |        6 |        9 |  241,852 *
Day 16 Ticket Translation                            |     1,624 |      777 |      223 |      624
Day 17 Conway Cubes (part 2 only)                    |     6,650 |       27 |        - |    6,622 *
Day 18 Operation Order (expression evaluation)       |     1,158 |       81 |      531 |      545
Day 19 Monster Messages (lexical token recognition)  |    31,792 |      506 |    4,575 |   26,710
Day 20 Jurassic Jigsaw (monochrome image processing) |    24,255 |      462 |   23,321 |      472
Day 21 Allergen Assessment (logic)                   |       914 |      135 |      768 |       11
Day 22 Crab Combat (recursive card game)             |    57,912 |       16 |       49 |   57,847
Day 23 Crab Cups (custom linked list)                |   952,192 |        6 |       32 |  952,154
Day 24 Lobby Layout (cellular automa 3)              |    14,046 |      595 |       13 |   13,439
Day 25 Combo Breaker                                 |     3,793 |       15 |    3,779 |        -

TOTAL: 1,373,169 μs
```

I've marked with `*` the ones that I've come back to optimise after solving the puzzle

Measurements notes:
 * Run on a i9-9980HK laptop
   * base clock = 2.4 GHz, but this chip boosts very high so expect variance due to this
 * "warm" timings, as this is the metric of most interest to me.
   (If the code was used anywhere that performance matters it would be JIT compiled.)
 * Excludes File-IO. (data is read into a `byte[]` before measurement)
 * Includes all parsing and puzzle-specific computation. E.g. if there is a Reg-Ex pattern, this
   is never cached in a static. I'm careful to include calling Pattern.compile within the
   measured time for a fair comparison.

Still TODO:
  * Some indication of variance. (I'm collecting a progressing of measurements over time,
    but currently just reporting the last.)

