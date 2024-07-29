package com.dropwizard.client;

import com.dropwizard.HibernateUtil;
import com.dropwizard.business.PokemonService;
import com.dropwizard.business.TrainerService;
import com.dropwizard.constant.PokemonType;
import com.dropwizard.dao.PokemonDAO;
import com.dropwizard.dao.TrainerDAO;
import com.dropwizard.dto.PokemonDTO;
import com.dropwizard.dto.TrainerDTO;
import com.dropwizard.model.Pokemon;
import com.dropwizard.model.Trainer;
import org.hibernate.SessionFactory;

import java.util.*;
import java.util.stream.Collectors;

public class PokemonTrainerFunctionalityMain {
    private static PokemonService pokemonService;
    private static TrainerService trainerService;

    public PokemonTrainerFunctionalityMain() {
        initialiseSessionFactory();
    }

    private static void initialiseSessionFactory() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        PokemonDAO pokemonDao = new PokemonDAO(sessionFactory);
        pokemonService = new PokemonService(pokemonDao);

        TrainerDAO trainerDao = new TrainerDAO(sessionFactory);
        trainerService = new TrainerService(trainerDao);
    }

    // 1. Get all trainers
    public List<TrainerDTO> getAllTrainers() {
        List<Trainer> trainers = trainerService.findAllTrainers();
        List<TrainerDTO> trainerDTOs = trainers.stream()
                .map(TrainerDTO::new)
                .collect(Collectors.toList());
        trainerDTOs.forEach(System.out::println);
        return trainerDTOs;
    }

    // 2. Add a new trainer
    public TrainerDTO addNewTrainer(String name, int age) {
        Trainer trainer = new Trainer();
        trainer.setName(name);
        trainer.setAge(age);
        trainer.setDateAdded(new Date()); // Set the current date and time
        trainerService.saveTrainer(trainer);
        TrainerDTO trainerDTO = new TrainerDTO(trainer);
        System.out.println("Trainer added: " + trainerDTO);
        return trainerDTO;
    }

    // 3. For a trainer using trainer ID get the trainer details
    public Optional<TrainerDTO> getTrainerById(Long id) {
        Optional<Trainer> trainer = trainerService.findTrainerById(id);
        return Optional.ofNullable(trainer.map(t -> {
            TrainerDTO trainerDTO = new TrainerDTO(t);
            System.out.println(trainerDTO);
            return trainerDTO;
        }).orElseGet(() -> {
            System.out.println("Trainer not found with ID: " + id);
            return null;
        }));
    }

    // 4. For a trainer using trainer ID get all their Pokémon details
    public List<PokemonDTO> getPokemonsByTrainerId(Long trainerId) {
        List<Pokemon> pokemons = pokemonService.findPokemonsByTrainerId(trainerId);
        List<PokemonDTO> pokemonDTOs = pokemons.stream()
                .map(PokemonDTO::new)
                .collect(Collectors.toList());
        if (pokemonDTOs.isEmpty()) {
            System.out.println("No Pokémon found for Trainer ID: " + trainerId);
        } else {
            pokemonDTOs.forEach(System.out::println);
        }
        return pokemonDTOs;
    }

    // 5. For a trainer using trainer ID get a specific Pokémon details using Pokémon ID
    public Optional<PokemonDTO> getPokemonByTrainerAndId(Long trainerId, Long pokemonId) {
        Optional<Pokemon> pokemon = trainerService.findPokemonByTrainerAndPokemonId(trainerId, pokemonId);
        return Optional.ofNullable(pokemon.map(p -> {
            PokemonDTO pokemonDTO = new PokemonDTO(p);
            System.out.println(pokemonDTO);
            return pokemonDTO;
        }).orElseGet(() -> {
            System.out.println("Pokémon not found for Trainer ID: " + trainerId + " and Pokémon ID: " + pokemonId);
            return null;
        }));
    }

    // 6. A trainer’s Pokémon might level up or the trainer may change their nickname
    public PokemonDTO updatePokemonDetails(Long trainerId, Long pokemonId, int newLevel, String newNickname) {
        Optional<Pokemon> pokemonOpt = trainerService.findPokemonByTrainerAndPokemonId(trainerId, pokemonId);
        return pokemonOpt.map(pokemon -> {
            pokemon.setLevel(newLevel);
            pokemon.setNickname(newNickname);
            pokemonService.savePokemon(pokemon); // Assuming save method updates if entity exists
            PokemonDTO pokemonDTO = new PokemonDTO(pokemon);
            System.out.println("Updated Pokémon details: " + pokemonDTO);
            return pokemonDTO;
        }).orElseGet(() -> {
            System.out.println("Pokémon not found for Trainer ID: " + trainerId + " and Pokémon ID: " + pokemonId);
            return null;
        });
    }

    // 7. A trainer may catch a Pokémon so add that to the trainer’s collection
    public void addPokemonToTrainer(Long trainerId, PokemonDTO pokemonDTO) {
        // Check if the Pokémon already exists
        Optional<Pokemon> existingPokemonOpt = pokemonService.findPokemonByNameLevelTypeAndGender(
                pokemonDTO.getName(),
                pokemonDTO.getLevel(),
                pokemonDTO.getPokemonTypes(),
                pokemonDTO.getGender()
        );

        if (existingPokemonOpt.isPresent()) {
            // Get the existing Pokémon
            Pokemon existingPokemon = existingPokemonOpt.get();

            // Remove the existing Pokémon from its previous trainer
            Long previousTrainerId = existingPokemon.getTrainer().getId();
            trainerService.releasePokemonFromTrainer(previousTrainerId, existingPokemon.getId());

            // Add the existing Pokémon to the new trainer
            trainerService.addPokemonToTrainer(trainerId, existingPokemon);
            System.out.println("Added existing Pokémon to Trainer ID: " + trainerId);
        } else {
            // Convert PokemonDTO to Pokemon and create a new Pokémon
            Pokemon pokemon = new Pokemon();
            pokemon.setName(pokemonDTO.getName());
            pokemon.setLevel(pokemonDTO.getLevel());
            pokemon.setNickname(pokemonDTO.getNickname());
            pokemon.setPokemonTypes(pokemonDTO.getPokemonTypes());
            pokemon.setGender(pokemonDTO.getGender());
            pokemon.setDateAdded(new Date());

            // Save the new Pokémon
            pokemonService.savePokemon(pokemon);

            // Add the new Pokémon to the new trainer
            trainerService.addPokemonToTrainer(trainerId, pokemon);
            System.out.println("Added new Pokémon to Trainer ID: " + trainerId);
        }
    }


    // 8. A trainer may release a Pokémon in the wild so the functionality to release the Pokémon from trainer’s collection
    public void releasePokemonFromTrainer(Long trainerId, Long pokemonId) {
        trainerService.releasePokemonFromTrainer(trainerId, pokemonId);
        System.out.println("Released Pokémon ID: " + pokemonId + " from Trainer ID: " + trainerId);
    }

    // 9. Given a Pokémon’s Pokédex ID fetch all trainers which have that Pokémon
    public List<TrainerDTO> getTrainersByPokemonPokedexId(Long pokedexId) {
        List<Trainer> trainers = trainerService.findTrainersByPokemonPokedexId(pokedexId);
        List<TrainerDTO> trainerDTOs = trainers.stream()
                .map(TrainerDTO::new)
                .collect(Collectors.toList());
        if (trainerDTOs.isEmpty()) {
            System.out.println("No trainers found with Pokémon Pokédex ID: " + pokedexId);
        } else {
            trainerDTOs.forEach(System.out::println);
        }
        return trainerDTOs;
    }

    // 10. Given a Pokémon’s ID fetch its trainer and details
    public Optional<TrainerDTO> getTrainerByPokemonId(Long pokemonId) {
        Optional<Trainer> trainer = trainerService.findTrainerByPokemonId(pokemonId);
        return Optional.ofNullable(trainer.map(t -> {
            TrainerDTO trainerDTO = new TrainerDTO(t);
            System.out.println(trainerDTO);
            return trainerDTO;
        }).orElseGet(() -> {
            System.out.println("Trainer not found for Pokémon ID: " + pokemonId);
            return null;
        }));
    }

    public static void main(String[] args) {
        PokemonTrainerFunctionalityMain main = new PokemonTrainerFunctionalityMain();

        // Add a new trainer
        main.addNewTrainer("Ash", 10);

        // Get all trainers
        main.getAllTrainers();

        // Get trainer by ID
        main.getTrainerById(1L);

        // Get Pokémon details for a trainer
        main.getPokemonsByTrainerId(1L);

        // Get specific Pokémon details for a trainer
        main.getPokemonByTrainerAndId(1L, 1L);

        // Update Pokémon details
        main.updatePokemonDetails(1L, 1L, 20, "Pikachu");

        // Add Pokémon to trainer
        PokemonDTO newPokemonDTO = new PokemonDTO();
        newPokemonDTO.setName("Bulbasaur");
        newPokemonDTO.setLevel(5); // Set initial level if needed
        newPokemonDTO.setNickname("Bulba"); // Set a nickname if desired
        newPokemonDTO.setDateAdded(new Date()); // Set the date added if needed

        // Initialize Pokémon types
        Set<PokemonType> types = new HashSet<>();
        types.add(PokemonType.ICE);
        types.add(PokemonType.FAIRY);
        newPokemonDTO.setPokemonTypes(types);

        // Add the new Pokémon to the trainer
        main.addPokemonToTrainer(1L, newPokemonDTO);

        // Release Pokémon from trainer
        main.releasePokemonFromTrainer(1L, 2L);

        // Get trainers by Pokémon Pokédex ID
        main.getTrainersByPokemonPokedexId(25L);

        // Get trainer by Pokémon ID
        main.getTrainerByPokemonId(1L);
    }
}
