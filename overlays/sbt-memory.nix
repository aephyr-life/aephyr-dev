final: prev: {
  sbt = prev.sbt.overrideAttrs (old: {
    postInstall = (old.postInstall or "") + ''
      wrapProgram $out/bin/sbt \
        --add-flags "-J-Xms512m -J-Xmx2g"
    '';
  });
}
