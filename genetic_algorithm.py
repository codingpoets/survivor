import logging
from numpy import random
import pprint as pp


log = logging.getLogger("GA")
MIN_WEIGHT_FACTOR = -4
MAX_WEIGHT_FACTOR = 4


class GeneticAlgorithm(object):
    def __init__(self, size_of_instance, generation_size=50):
        self.generation_size = generation_size
        self.size_of_instance = size_of_instance
        self.population = self.generate_random_population()
        self.normalized_fitness_results = None

    def announce_normalized_fitness_results(self, normalized_fitness_results):
        self.normalized_fitness_results = normalized_fitness_results

    def generate_random_population(self):
        population = []
        for _ in range(self.generation_size):
            population.append(tuple(random.uniform(MIN_WEIGHT_FACTOR, MAX_WEIGHT_FACTOR, size=self.size_of_instance)))
        return population

    @staticmethod
    def calc_normalized_fitness_results(results):
        total_score = sum(results)
        return [res / total_score for res in results]

    def create_mating_pool(self, normalized_fitness_results, population, size=None):
        if size is None:
            size = self.generation_size
        indices = range(len(population))
        mating_pool = random.choice(indices, size=size, p=normalized_fitness_results)
        return [population[i] for i in mating_pool]

    @staticmethod
    def reproduce(parent1, parent2, crossover_point=None):
        if crossover_point is None:
            # if crossover point not given, then find random crossover point
            crossover_point = random.randint(len(parent1))
        print("Crossover point at: {}".format(crossover_point))
        parent1 = list(parent1)
        parent2 = list(parent2)
        # combine parents into child with crossover point
        child_1 = parent1[:crossover_point] + parent2[crossover_point:]
        child_2 = parent2[:crossover_point] + parent1[crossover_point:]
        return child_1, child_2

    @staticmethod
    def create_mutation(child):
        # perform random permutation in child
        probability_for_mutation = 0.05
        if random.random() > probability_for_mutation:
            return child
        new_child = list(child)
        print("Performing mutation!")
        print("Child before: {}".format(child))
        idx = random.randint(0, len(new_child))
        new_child[idx] = random.uniform(MIN_WEIGHT_FACTOR, MAX_WEIGHT_FACTOR)
        print("Child after: {}".format(new_child))
        return new_child

    def get_population(self):
        return self.population

    def generate_next_generation(self, size=None, number_randoms=0):
        if size is None:
            size = self.generation_size
        reproduction_size = size - number_randoms
        mating_pool = self.create_mating_pool(self.normalized_fitness_results, self.population, reproduction_size)
        log.info("Mating pool is: {}".format(mating_pool))
        next_generation = []
        for i in range(int(len(mating_pool) / 2)):
            children = self.reproduce(mating_pool[i*2], mating_pool[i*2 + 1])
            mutated_children = [self.create_mutation(child) for child in children]
            next_generation.extend(mutated_children)
        # generate random children
        for _ in range(number_randoms):
            random_child = tuple(random.uniform(MIN_WEIGHT_FACTOR, MAX_WEIGHT_FACTOR, size=self.size_of_instance))
            next_generation.append(random_child)
        self.population = next_generation

    def save_generation(self, file_path, scores):
        assert (len(self.population) == len(scores))
        with open(file_path, 'w') as outfile:
            outfile.write("SCORES;" + ";".join(["VALUE{}".format(i) for i in range(len(self.population[0]))]) + "\n")
            for i in range(len(scores)):
                outfile.write(str(scores[i]) + ";")
                outfile.write(";".join([str(val) for val in self.population[i]]) + "\n")

    def load_generation(self, file_path):
        with open(file_path, 'r') as infile:
            lines = [line.strip().split(';') for line in infile.readlines()]
        scores = [float(line[0]) for line in lines[1:]]
        population_data = [tuple([float(val) for val in line[1:]]) for line in lines[1:]]
        self.population = population_data
        self.normalized_fitness_results = self.calc_normalized_fitness_results(scores)


if __name__ == '__main__':
    logging.basicConfig(level=logging.DEBUG,
                        handlers=[logging.FileHandler("ga.log"),
                                  logging.StreamHandler()])

    population = [("A", "A"), ("B", "B"), ("C", "C"), ("D", "D"), ("E", "E"), ("F", "F")]
    ga = GeneticAlgorithm(size_of_instance=2, generation_size=6)
    normalized_fitness_results = [0.6, 0.2, 0.1, 0.05, 0.025, 0.025]
    ga.announce_normalized_fitness_results(normalized_fitness_results)
    next_generation = ga.generate_next_generation()
    log.info("Next generation is: {}".format(pp.pformat(next_generation)))