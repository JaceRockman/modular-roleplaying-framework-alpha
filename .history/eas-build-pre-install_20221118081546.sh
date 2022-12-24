if [[ $OSTYPE == "darwin"* ]]; then
  brew install openjdk@11
  sudo ln -sfn /usr/local/opt/openjdk@11/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-11.jdk
fi