package vehicleRoutingCore;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.api.score.ScoreExplanation;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.score.constraint.ConstraintMatchTotal;
import org.optaplanner.core.api.solver.SolutionManager;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;

import vehicleRoutingCore.domain.Solution;
import vehicleRoutingCore.domain.VehicleRoutingSolution;

public class ProblemSolver {
	
	private Solver<VehicleRoutingSolution> solver;

	private SolverConfig solverConfig;
	
	public Solution solve(VehicleRoutingSolution problem) {
		
		SolverFactory<VehicleRoutingSolution> solverFactory = SolverFactory.create(solverConfig);
		
		solver = solverFactory.buildSolver();
		
		VehicleRoutingSolution vehicleRoutingSolution = solver.solve(problem);

		SolutionManager<VehicleRoutingSolution, HardSoftLongScore> scoreManager = SolutionManager.create(solverFactory);
		ScoreExplanation<VehicleRoutingSolution, HardSoftLongScore> scoreExplanation = scoreManager.explain(vehicleRoutingSolution);
		
		List<String> errors = new ArrayList<>();
		
		for(Object key : scoreExplanation.getConstraintMatchTotalMap().keySet()) {
			ConstraintMatchTotal<HardSoftLongScore> constraint = scoreExplanation.getConstraintMatchTotalMap().get(key);
			if(constraint.getScore().getHardScore() < 0L) {
				errors.add(constraint.getConstraintName());
			}
		}
		
		Solution solution = new Solution(vehicleRoutingSolution, errors);
		
				
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
