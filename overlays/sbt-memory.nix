final: prev: {
  sbt = prev.sbt.overrideAttrs (old: {
    nativeBuildInputs = (old.nativeBuildInputs or []) ++ [ prev.makeWrapper ];
    postInstall = (old.postInstall or "") + ''
      if [ -x "$out/bin/sbt" ]; then
        wrapProgram "$out/bin/sbt" \
          --add-flags "-J-Xms512m -J-Xmx2g"
      fi
      if [ -x "$out/bin/sbtn" ]; then
        wrapProgram "$out/bin/sbtn" \
          --add-flags "-J-Xms512m -J-Xmx2g"
      fi
    '';
  });
}
