## What is Easy Rules?
[![Gitter](https://badges.gitter.im/Join Chat.svg)](https://gitter.im/benas/easy-rules?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Easy Rules is a simple yet powerful Java Rules Engine providing the following features :

 * Lightweight framework and easy to learn API

 * POJO based development with annotation programming model

 * Useful abstractions to define business rules and apply them easily using Java

 * The ability to create composite rules from primitive ones

 * Dynamic rule reconfiguration at runtime using JMX

## Presentation

You can find some slides about Easy Rules on [speaker deck][].

## Why this fork?
Easy Rules is a great lightweight rules engine, but the support for JMX makes it incompatible with Android. In short this is due to the fact that the java.lang.management library depends on the JVM and Android runs off of the DVM. This forked project removes the dependency on this library so it can be used by an Android application.

## Documentation

All Easy Rules documentation can be found here : [http://www.easyrules.org][]

## License
Easy Rules is released under the [MIT License][].

[speaker deck]: https://speakerdeck.com/benas/easy-rules
[http://www.easyrules.org]: http://www.easyrules.org
[MIT License]: http://opensource.org/licenses/mit-license.php/
