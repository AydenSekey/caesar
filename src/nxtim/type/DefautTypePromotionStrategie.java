/*
Copyright (C) Université du Maine (2013) 

contributeurs : Adrien Duroy, Bastien Andru, Quentin Gosselin, Guillaume Delorme,
 Nicolas Detan, Zubair Parwany, Houda Chouket, Bastien Aubry,
 Vincent Besnard, Ivan Melnychenko

ad.duroy@gmail.com

Ce fichier est une partie de NXTIM.

NXTIM est une bibliothèque logiciel fournissant un modèle objet du code d'un programme
pour un robot NXT. 

NXTIM est régi par la licence CeCILL-C soumise au droit français et
respectant les principes de diffusion des logiciels libres. Vous pouvez
utiliser, modifier et/ou redistribuer ce programme sous les conditions
de la licence CeCILL-C telle que diffusée par le CEA, le CNRS et l'INRIA 
sur le site "http://www.cecill.info".

En contrepartie de l'accessibilité au code source et des droits de copie,
de modification et de redistribution accordés par cette licence, il n'est
offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
seule une responsabilité restreinte pèse sur l'auteur du programme,  le
titulaire des droits patrimoniaux et les concédants successifs.

A cet égard  l'attention de l'utilisateur est attirée sur les risques
associés au chargement,  à l'utilisation,  à la modification et/ou au
développement et à la reproduction du logiciel par l'utilisateur étant 
donné sa spécificité de logiciel libre, qui peut le rendre complexe à 
manipuler et qui le réserve donc à des développeurs et des professionnels
avertis possédant  des  connaissances  informatiques approfondies.  Les
utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
logiciel à leurs besoins dans des conditions permettant d'assurer la
sécurité de leurs systèmes et ou de leurs données et, plus généralement, 
à l'utiliser et l'exploiter dans les mêmes conditions de sécurité. 

Le fait que vous puissiez accéder à cet en-tête signifie que vous avez 
pris connaissance de la licence CeCILL-C, et que vous en avez accepté les
termes.
 */
package nxtim.type;

import java.util.ArrayList;
import java.util.List;

/**
 * Stratégie de promotion par défaut. <br>
 * Règles de promotions :<br>
 * short -> int -> long <br>
 * float -> double <br>
 * entier -> flottant
 */
public class DefautTypePromotionStrategie implements TypePromotionStrategie {

	@Override
	public boolean isPromouvable(TypeElement type) {
		switch(type) {
			case SHORT:
			case INT:
			case FLOAT:
			case LONG:
				return true;
			default:
				break;
		}
		return false;
	}

	@Override
	public List<TypeElement> nextPromotions(TypeElement type) {
		List<TypeElement> promotions = new ArrayList<TypeElement>();
		switch(type) {
			case SHORT:
				promotions.add(TypeElement.INT);
				break;
			case INT:
				promotions.add(TypeElement.LONG);
				promotions.add(TypeElement.FLOAT);
				break;
			case LONG:
				promotions.add(TypeElement.FLOAT);
				break;
			case FLOAT:
				promotions.add(TypeElement.DOUBLE);
				break;
			default:
				break;
		}
		return promotions;
	}

	@Override
	public boolean isPromouvableEn(TypeElement typeSrc, TypeElement typeCible) {
		boolean promouvableEn = false;
		if(typeSrc == typeCible) {
			promouvableEn = true;
		} else {
			List<TypeElement> promotions = nextPromotions(typeSrc);
			if(promotions.contains(typeCible)) {
				promouvableEn = true;
			} else {
				for(TypeElement type : promotions) {
					promouvableEn = isPromouvableEn(type, typeCible);
					if(promouvableEn)
						break;
				}
			}
		}
		return promouvableEn;
	}

}
