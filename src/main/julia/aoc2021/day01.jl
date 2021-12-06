day = "01"
lines = readlines("src/main/resources/2021/in_$day.txt")
ints = [parse(Int, x) for x in lines]
part1 = sum(ints[n] > ints[n-1] for n in 2:length(ints))

function sum3(n)
    ints[n-2] + ints[n-1] + ints[n]
end
part2 = sum(sum3(n) > sum3(n-1) for n in 4:length(ints))

(part1, part2)
