#!/bin/bash

function report {
cat <<HTML
<html>
<head>
  <style>
    body {
      background-color: white;
      font-family: Lato;
      margin-left: 35px;
      margin-top: 15px;
    }

    .name {
      display: inline-block;
      width: 150px;
    }

    .value {
      display: inline;
    }

    .hash, pre {
      font-size: 15px;
      font-family: "Roboto Mono", Menlo;
    }

    a {
      text-decoration: none;
      color: #3c3c3c;
    }

    .file, .hash, .signature {
      color: black;
      margin-bottom: 10px;
      display: inline-block;
    }

    .file {
      margin-bottom: 15px;
      text-decoration: none;
      font-size: 20px;
      color: #3c3c3c;
      font-weight: 400;
      -webkit-font-smoothing: antialiased;
    }

    .files {
      margin-bottom: 20px;
    }

    .input {
      margin-bottom: 10px;
    }
    .hash {
      padding-left: 10px;
      border-left: 4px solid #d63aff;
      color: #d63aff;
      background: #d63aff24;
      width: 588px;
      padding-top: 7px;
      padding-bottom: 7px;
    }

    .signature {
      border-left: 4px solid #00b9f1;
      margin-top: 10px;
      background: #00b9f124;
    }

    .release {
      font-size: 30px;
      font-weight: 300;
      margin-bottom: 30px;
    }
    pre {
      margin: 0px;
      /* background: #eaeaea; */
      padding: 10px;
      display: inline-flex;
      color: #00b9f1;
    }

    h2 {
      margin-left: -10px;
    }
  </style>
</head>
<body>

<h2>DATE</h2>

<div>$(date)</div>

<h2>INPUT</h2>

<div class="run">
  <div class="input">
    <div class="name">SPEC_NAME</div>
    <div class="value">$SPEC_NAME</div>
  </div>
  <div class="input">
    <div class="name">SPEC_VERSION</div>
    <div class="value">$SPEC_VERSION</div>
  </div>
  <div class="input">
    <div class="name">FILE_URLS</div>
    <div class="value">$FILE_URLS</div>
  </div>
</div>

<h2>RELEASE</h2>

<div class="release">
  <a href="https://download.eclipse.org/jakartaee/$SPEC_NAME/$SPEC_VERSION/">https://download.eclipse.org/jakartaee/$SPEC_NAME/$SPEC_VERSION/</a>
</div>

<h2>PROMOTED FILES</h2>

HTML

for file in "$@"; do
cat <<HTML
<div class="files">
  <div>
    <div class="file">
      <a href="https://download.eclipse.org/jakartaee/$SPEC_NAME/$SPEC_VERSION/$file">$file</a>
    </div>
  </div>
  <div>
    <div class="hash">$(cat $file.sha256)</div>
  </div>
  <div>
    <div class="signature"><pre>$(cat $file.sig)</pre>
    </div>
  </div>
</div>
HTML
done

cat <<HTML
<h2>JAKARTA EE PUBLIC KEY</h2>

<div class="publickey">
<pre>$(gpg --armor --export 'jakarta.ee-spec@eclipse.org')</pre>
</div>
</body>
</html>
HTML
}