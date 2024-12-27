y ?= `date +'%Y'`
d ?= `date +'%d'`
tomd := `date -v +1d +'%d'`

.PHONY: generate
generate:
	mkdir -p src/main/kotlin/y${y}/d${d}
	cp -R src/main/kotlin/template/ src/main/kotlin/y${y}/d${d}/ &&\
	sed -i '' "s/package template/package y${y}\.d${d}/g" src/main/kotlin/y${y}/d${d}/Main.kt &&\
	sed -i '' "s/template/y${y}\/d${d}/g" src/main/kotlin/y${y}/d${d}/Main.kt

.PHONY: generate-tomorrow
generate-tomorrow:
	mkdir -p src/main/kotlin/y${y}/d${d}
	cp -R src/main/kotlin/template/ src/main/kotlin/y${y}/d${tomd}/ &&\
	sed -i '' "s/package template/package y${y}\.d${tomd}/g" src/main/kotlin/y${y}/d${tomd}/Main.kt &&\
	sed -i '' "s/template/y${y}\/d${tomd}/g" src/main/kotlin/y${y}/d${tomd}/Main.kt

.PHONY: time-today
time-today:
	date -r src/main/kotlin/y${y}/d${d}/Input-test.txt
	date -r src/main/kotlin/y${y}/d${d}/Input.txt
	date -r src/main/kotlin/y${y}/d${d}/Main.kt
	@START=$$(date -r src/main/kotlin/y${y}/d${d}/Input.txt +%s) && \
	END=$$(date -r src/main/kotlin/y${y}/d${d}/Main.kt +%s) && \
	DIFF_SUB_S=$$((($$END - $$START)%60)) && \
	DIFF_SUB_M=$$((($$END - $$START)/60)) && \
	echo && \
	echo it took $${DIFF_SUB_M} minutes and $${DIFF_SUB_S} seconds
