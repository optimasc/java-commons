java-commons
============

Introduction
------------

The Java common utilities and datatype management libraries are a set of
subprojects composed of generic utility classes. The objective of these
libraries is to be compatible with Java 1.4 and, in some cases, with the
CLDC and CDC embedded Java environments, so that they can be used in older
Java environments as well as in embedded ones.

Modules
-------

| Directory | Contents | API restriction |
|-----------|----------|-----------------|
| `optimasc-commons-cldc` | Base classes for basic I/O, error management, archives, text and character set conversion. | CLDC 1.1 (JSR 139) |
| `optimasc-commons-cdc` | I/O, parsing, text processing, mathematics and date conversion utilities. | CDC 1.1 (JSR 218) with Foundation Profile 1.1 (JSR 219) |
| `optimasc-datatypes` | Basic datatype class representations, used to represent schemas such as SQL, XML Schema, ASN.1 types or ISO/IEC 11404 general purpose datatypes. | Java SE 5 |
| `optimasc-schemas` | Schema definition objects. | Java SE 5 |

The code of all modules except `optimasc-schemas` uses the Java 1.4 language
level, so it does not use generics, annotations or enumerations.

Requirements
------------

* A JDK whose compiler accepts the Java 1.4 and 1.5 source and target levels.
  The original Java 1.4 JDK is the reference compiler, and JDK 8 has also been
  used. Later JDKs removed support for these levels and cannot be used. Its
  location is given in the `JAVA1_4_HOME` property.
* Maven 3.2.5 or later. The build has been verified with Maven 3.9.11 run with
  JDK 8.
* GnuPG, only to sign and deploy release artifacts.

Building
--------

From the root of the repository:

    mvn -DJAVA1_4_HOME=/path/to/jdk package

This builds and tests all modules. The `jdk14` profile (`-P jdk14`)
retrotranslates the classes to the Java 1.4 class file format, and the
`release` profile (`-P release`) signs the artifacts and checks the API usage
against the target environment of each module. See the
[SPS-CMP document](doc/SPS-CMP.html) for the full build procedure.

Documentation
-------------

* [SPS-CMP](doc/SPS-CMP.html): the software product specification and
  configuration management plan, which defines the build, versioning,
  coding, documentation and testing rules of this project.
* The list of changes of each module is in its `src/changes/changes.xml` file.

License
-------

This project is distributed under the Apache License 2.0.
