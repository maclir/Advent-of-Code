y := `date +'%Y'`
d := `date +'%d'`
tomd := `date -v +1d +'%d'`

.PHONY: generate-today
generate-today:
	cp -R src/main/kotlin/template src/main/kotlin/y${y}/d${d} &&\
	sed -i '' "s/package template/package y${y}\.d${d}/g" src/main/kotlin/y${y}/d${d}/Main.kt &&\
	sed -i '' "s/template/y${y}\/d${d}/g" src/main/kotlin/y${y}/d${d}/Main.kt

.PHONY: generate-tomorrow
generate-tomorrow:
	cp -R src/main/kotlin/template src/main/kotlin/y${y}/d${tomd} &&\
	sed -i '' "s/package template/package y${y}\.d${tomd}/g" src/main/kotlin/y${y}/d${tomd}/Main.kt &&\
	sed -i '' "s/template/y${y}\/d${tomd}/g" src/main/kotlin/y${y}/d${tomd}/Main.kt
