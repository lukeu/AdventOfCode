function countOnes(input)
    # Note: 'length' assumes all strings are ASCII / 1-byte chars
    len = length(first(input))
    counts = zeros(Int, len)
    for s in input
        for i in eachindex(s)
            if s[i] == '1'
                counts[i] += 1
            end
        end
    end
    counts
end

day = "03"
lines = readlines("src/main/resources/2021/in_$day.txt")

function go()
    gamma = ""
    eps = ""

    function parse2(s::String)
        parse(Int, s, base = 2)
    end

    function p1()
        counts = countOnes(lines)
        most = ""; least = ""

        for c in counts
            more = (c >= (length(lines) / 2))
            most *= more ? '1' : '0'
            least *= more ? '0' : '1'
        end
        println(most)
        println(least)

        println(string("p1: ", parse2(most) * parse2(least)))
    end

    function find(isMost)
        matches = Set(lines)
        test = ""
        n::Int = 1
        while (length(matches) > 1)
            counts = countOnes(matches)
            moreOnes = counts[n] >= (length(matches)/2)
            test *= (moreOnes ⊻ isMost) ? '0' : '1'
            filter!(s -> SubString(s, 1, n) == test, matches)
            println(string(test, " > ", length(matches)))
            n += 1
        end
        return first(matches)
    end

    p1()
    println(string("p2: ", parse2(find(true)) * parse2(find(false))))
end

go()
