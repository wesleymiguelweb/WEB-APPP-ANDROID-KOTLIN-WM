# FilmesPlay

Aplicativo Android em Kotlin/XML para cinema, com filmes em cartaz, programação, loja de produtos e área administrativa.

## Como executar

1. Clone ou baixe este repositório.
2. Abra a pasta raiz do projeto no Android Studio.
3. Aguarde o Gradle sincronizar.
4. Execute o app pelo botão Run.

Abra a pasta que contém:

```text
settings.gradle.kts
build.gradle.kts
app/
gradle/
gradlew.bat
```

## Estrutura

```text
app/src/main/java/com/example/filmesplay
```

Principais áreas:

- `MainActivity`: tela inicial pública.
- `FilmesActivity`: lista de filmes.
- `ProgramacaoActivity`: sessões disponíveis.
- `LojaActivity`: produtos e seleção demonstrativa.
- `AdminActivity`: entrada para administrar dados.
- `ApiService` e `RetrofitClient`: conexão com a API PHP.

## API

O app consome uma API PHP configurada em:

```text
app/src/main/java/com/example/filmesplay/RetrofitClient.kt
```

Endpoints esperados:

```text
filmes.php
programacao.php
produtos.php
login.php
registro.php
incluir_filme.php
editar_filme.php
deletar_filme.php
incluir_programacao.php
editar_programacao.php
deletar_programacao.php
incluir_produto.php
editar_produto.php
deletar_produto.php
```

## Observação

Arquivos locais como `.idea/`, `.gradle/`, `.kotlin/`, `build/` e `local.properties` não devem ser enviados para o GitHub.
