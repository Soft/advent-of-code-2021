{ pkgs ? import <nixpkgs> {} }:

with pkgs;

mkShell {
  buildInputs = [
    adoptopenjdk-bin
    clojure
    leiningen
  ];

  shellHook = ''
  # https://github.com/technomancy/leiningen/issues/2611
  export LEIN_JVM_OPTS=
  '';
}
