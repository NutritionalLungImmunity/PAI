---
title: 'The Pulmonary Agent-based Infection simulator (PAI): A Multi-Scale Agent-Based Model of Pulmonary Host-Pathogen Interactions'
tags:
  - Agent-Based-Model
  - Infection
  - Immune System
  - Computational Biology
authors:
  - name: Henrique AL Ribeiro
    orcid: 0000-0001-9802-5211
    affiliation: 1 
    corresponding: true
  - name: Sandra A Tsiorintsoa
    orcid: 0000-0002-7925-987X
    affiliation: 1
  - name: Reinhard Laubenbacher
    affiliation: 1
    orcid: 0000-0002-9143-9451
affiliations:
  - name: Department of Medicine, Division of Pulmonary, Critical Care, and Sleep Medicine, University of Florida, Gainesville 32610, FL, USA.
    index: 1

date: 12 June 2025
bibliography: paper.bib
---

# Summary

Understanding the spatial and regulatory dynamics of the immune response to pulmonary infections remains a central challenge in computational immunology. While mechanistic models, such as ODEs and PDEs, offer insights into intracellular processes, they are limited in representing discrete behaviors and spatial heterogeneity. The pulmonary Agent-based Infection simulator (PAI) addresses this gap through an extensible, multi-scale agent-based modeling designed to simulate lung immune responses to pathogens such as *Aspergillus fumigatus*.

PAI operates within a three-dimensional voxelized space that approximates the alveolar microenvironment, incorporating host and fungal cells. It models inter- and intracellular signaling, immune cell recruitment and movement, and pathogen nutrient acquisition.

PAI is implemented in both Java (jPAI) and C++ (PAI++), with identical outputs and comparable performance, though PAI++ significantly reduces memory usage. With its modular architecture and biological fidelity, PAI provides a valuable tool for conducting in silico experiments where empirical approaches are limited.

# Statement of Need

Invasive Pulmonary aspergillosis is a human infection with increasing incidence in immunosuppressed patients such as those receiving chemotherapy or organ transplants (@pappas2010). It has also been observed that 10\%-14\% of COVID-19 patients in the ICU develop invasive aspergillosis (@mitaka2021; @chong2021). Despite advances in diagnostics and therapy, mortality remains as high as 30–60\% in recent surveys (@neofytos2013).

Understanding the complex interplay between immune cells, pathogens, and signaling molecules during pulmonary infections requires computational models that capture both the spatial and regulatory dynamics of the immune response (@minucci2020; @yue2022). While mechanistic models such as ODEs or PDEs are effective at simulating intracellular or population-level behavior, they lack the flexibility to represent discrete cellular interactions and heterogeneous spatial environments.

![Figure 1: ABM Architecture. A: Overall architecture. The “Exec” iterates through the grid-of-voxels (green) and executes the public methods of Voxel (blue). The Voxels iterates through their local lists of agents (orange bottom), executing their public methods. Exec also executes molecule diffusion, “garbage collection (gc)” of dead cells, and cell recruitment. B: Diagram showing the agent’s classes. Black arrows represent inheritance, while red represents composition. All agents are interactables, which means that they can potentially interact with any other agents. The class Interactions provides a library of pairwise agent interaction methods to be called by the agents. \label{fig:architecture}](architecture.png)

PAI addresses this gap by providing a scalable, extensible agent-based modeling framework for simulating the tissue-scale immune response to lung infections, such as *Aspergillus fumigatus*. PAI has been used before, and the mathematical and biological components are better explained in @ribeiro2022. While in @ribeiro2023, we modified it to simulate and generate a hypothesis about Covid-Associated-Pulmonary-Aspergillosis. @qu2025 expanded the original model to simulate the effect of NETosis and hemorrhage on Aspergillosis. However, none of these papers provides detailed information about the software. 

# Model

## Architecture

As shown in Figure 1A, at the top level, a Main module orchestrates the initialization and execution of simulations, including setting up initial conditions, managing output, and controlling the simulation loop. Initialization is handled by a dedicated Initializer class, which instantiates the molecular fields, populates the simulation space with agents, and assigns intracellular models where appropriate.

The simulation domain is discretized into a three-dimensional voxel grid, where each voxel represents a microenvironment (~40 µm per side) and maintains localized lists of agents. The Voxel class governs local interactions (interact method), molecular degradation (degrade method), and cell updates (update method) and is invoked iteratively by the Exec class. The Exec class also handles molecular diffusion (via PDE solvers), cellular recruitment (based on chemokine gradients), and agent-level garbage collection.

Agents in the PAI are broadly categorized as Cells or Molecules, both inheriting from an abstract Interactable class (Figure 1B). This base class implements a symmetric interact method based on an “inductive” architecture: new agent types define how they interact with existing agents. If Agent “A” does not provide code to interact with Agent “B,” nor does Agent “B” provide code to interact with “A,” they learn on the fly that they do not interact (Figure 2).

![Figure 2: Dynamics of interaction between two agents. Decision diagram made by the methods “interact” and “templateInteract” from the two classes trying to interact (“A” and “B”). \label{fig:inductive}](InductiveArchitecture.png)

## Instantiation

A typical instantiation of the PAI model, as used in @ribeiro2022, involves a 10×10×10 voxel grid populated with both cellular and molecular agents. The simulation initializes molecular fields, Transferrin, Lactoferrin, TAFC, Iron, IL-6, TNF-α, IL-10, TGF-β, CXCL2, CCL4, and Hepcidin. It is also initialized with 640 Type II pneumocytes, 15 macrophages, 15 neutrophils, and 1920 *Aspergillus fumigatus* (in the resting conidia state) randomly distributed across the grid. The simulation proceeds via the Exec class, which iterates over all voxels and over the lists of agents they contain. Simulations typically run for 2160 iterations (approximately 72 hours), as in @ribeiro2022, where results were quantitatively compared with *in vivo* data and demonstrated close agreement.

![Figure 3: jPAI and PAI++ comparison. A: Macrophage time series. B: TNF-a time series. C: Running time. D: Memory usage. Memory recorded with Valgrind (C++) and JProfiler (Java). All figures represent the average of 100 simulations. Simulations initialized with 10X10X10 voxels, 640 pneumocytes, 1920 conidia, 15 macrophages, and 2160 iterations. PAI++ compiled with g++ 12.4.0 using the MinGW toolchain with level three optimization; jPAI run with Java 23.0.1. Execution environment: Windows 10 Education 64-Bit Dell laptop, Intel® Core™ Ultra 7 155U (14CPUs), ~2.1GHz, 16384MB RAM. A-B: paired t-test. C-D: Wilcoxon rank-sum test. (ns not significant p > 0.05; **** p < 0.0001). \label{fig:benchmark}](Benchmark.png)

## Benchmark

PAI++ contains only the code of the @ribeiro2022 model, which is the focus of this paper, while jPAI contains other pieces of code not discussed here. The code used for this benchmark is a simplification of @ribeiro2022. Within this context, PAI++ and jPAI are nearly identical (Figure 3A-B). The similarity between the two implementations serves as an important benchmark for model reproducibility, a key target in the modeling community (@donkin2017; @masison2021). Figure 3C shows that the running time (wall time) of both implementations is similar (~11 sec), with C++ being slightly faster. On the other hand, there were major differences in the memory usage (Figure 3D).

We provided documentation only for the Java version of the code. However, a C++ code follows the same structure.


# References

