package org.rti.bioinformatics

import java.net.URI
import java.util.UUID

import scala.collection.JavaConverters._

import org.apache.jena.graph.compose.MultiUnion
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.util.FileUtils
import org.topbraid.shacl.arq.SHACLFunctions
import org.topbraid.shacl.constraints.ModelConstraintValidator
import org.topbraid.shacl.util.ModelPrinter
import org.topbraid.shacl.vocabulary.SH
import org.topbraid.spin.arq.ARQFactory
import org.topbraid.spin.util.JenaUtil
import org.topbraid.spin.util.SystemTriples

object RunSHACL extends App {

  val ontologyModel = ModelFactory.createDefaultModel()
  // Load the ontology to be tested
  ontologyModel.read(args(0))
  val shapesModel = ModelFactory.createDefaultModel()
  // Load the file of shape constraints
  shapesModel.read(args(1))
  val dataset = ARQFactory.get.getDataset(ontologyModel)

  // Load the core SHACL definitions
  val shaclModel = JenaUtil.createDefaultModel();
  val shaclTTL = getClass().getResourceAsStream("/etc/shacl.ttl");
  shaclModel.read(shaclTTL, SH.BASE_URI, FileUtils.langTurtle);
  val dashTTL = getClass().getResourceAsStream("/etc/dash.ttl");
  shaclModel.read(dashTTL, SH.BASE_URI, FileUtils.langTurtle);
  shaclModel.add(SystemTriples.getVocabularyModel());
  SHACLFunctions.registerFunctions(shaclModel);

  // Combine local shape constraints with core SHACL definitions
  val unionGraph: MultiUnion = new MultiUnion(Array(
    shaclModel.getGraph,
    shapesModel.getGraph))
  val unionShapesModel = ModelFactory.createModelForGraph(unionGraph)

  // Register core SHACL functions
  SHACLFunctions.registerFunctions(unionShapesModel)

  val shapesGraphURI = URI.create("urn:x-shacl-shapes-graph:" + UUID.randomUUID.toString)
  dataset.addNamedModel(shapesGraphURI.toString, unionShapesModel)

  // Validate dataset against constraints in shapes graph
  val results = ModelConstraintValidator.get.validateModel(dataset, shapesGraphURI, null, true, null, null)
  // Need a much better result printer
  if (!results.isEmpty()) {
    println(ModelPrinter.get.print(results))
    System.exit(1)
  }

}