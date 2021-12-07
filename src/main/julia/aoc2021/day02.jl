day = "02"
lines = readlines("src/main/resources/2021/in_$day.txt")

tuples = [(s = split(x); (dir=s[1], n=parse(Int, s[2]))) for x in lines]

function go()
    x = 0; y = 0; aim = 0
    for t in tuples
        if (t.dir == "down")
            aim += t.n
        elseif (t.dir == "up")
            aim -= t.n
        else
            x += t.n
            y += (aim * t.n)
        end
    end
    println(string("p1: ", (aim * x)))
    println(string("p2: ", (y * x)))
end

go()

