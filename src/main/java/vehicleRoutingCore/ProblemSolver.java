package vehicleRoutingCore;

import java.io.File;
import java.time.Duration;

import org.optaplanner.core.api.score.ScoreExplanation;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.solver.SolutionManager;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;

import vehicleRoutingCore.domain.VehicleRoutingSolution;

public class ProblemSolver {
	
	private Solver<VehicleRoutingSolution> solver;

	private SolverConfig solverConfig;
	
	public VehicleRoutingSolution solve(VehicleRoutingSolution problem) {
		
		SolverFactory<VehicleRoutingSolution> solverFactory = SolverFactory.create(solverConfig);
		
		solver = solverFactory.buildSolver();
		
		VehicleRoutingSolution solution = solver.solve(problem);

		SolutionManager<VehicleRoutingSolution, HardSoftScore> scoreManager = SolutionManager.create(solverFactory);
		ScoreExplanation<VehicleRoutingSolution, HardSoftScore> scoreExplanation = scoreManager.explain(solution);
		
		System.out.println(scoreExplanation.getSummary());
		
		
				
		return solution;
	}
	
	public void configSolver(Long time) {
		
		File file = new File("/home/francisco/wap-workspace/vehiclerouting/resources/solverConfig.xml");
		
		solverConfig = SolverConfig.createFromXmlFile(file);
		solverConfig.withTerminationSpentLimit(Duration.ofSeconds(time));
		//solverConfig = new SolverConfig()
//						   .withSolutionClass(VehicleRoutingSolution.class)
//						   .withEntityClasses(Vehicle.class, Entity.class)
//						   .withConstraintProviderClass(VehicleRoutingConstraintProvider.class)
//						   .withTerminationSpentLimit(Duration.ofSeconds(30))
						   //.withPhases(constructionHeuristicPhaseConfig)
						   //.withScoreDirectorFactory(scoreDirectorFactoryConfig)
//						   .withConstraintStreamImplType(ConstraintStreamImplType.BAVET);
		
						   //.withPhases(localSearchPhaseConfig);
		
		
		
		
//		scoreDirectorFactoryConfig.setConstraintProviderClass(VehicleRoutingConstraintProvider.class);
//		scoreDirectorFactoryConfig.setConstraintStreamImplType(ConstraintStreamImplType.BAVET);
//		solverConfig.setScoreDirectorFactoryConfig(scoreDirectorFactoryConfig);
		
		
	}
	
}
